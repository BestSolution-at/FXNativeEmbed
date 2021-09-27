/**
 * Copyright (C) 2020 - BestSolution.at
 */
package at.bestsolution.fxembed.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKStage;

import at.bestsolution.fxembed.swing.win32.WindowsNative;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Embed JavaFX Natively in Swing/AWT as a <strong>Heavyweight</strong>
 * component
 */
@SuppressWarnings({ "restriction" })
public class FXEmbed extends JComponent {
	
	private static final Logger logger = LoggerFactory.getLogger(FXEmbed.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static boolean FX_LAUNCHED;
	private static CountDownLatch START_LATCH = new CountDownLatch(1);
	private static Map<Window, List<Stage>> STAGES = new WeakHashMap<>();

	private volatile long fxHandle;
	private Stage stage;
	private TKSceneListener sceneListener;

	private final ComponentAdapter componentListener = new ComponentAdapter() {
		@Override
		public void componentMoved(ComponentEvent e) {
			desktopPositionChanged();
		}
	};

	private FXEmbed() {
		logger.info("[FXEmbed] initialize component...");
		System.setProperty("fxembed.version", VersionInfoUtil.getVersion());
		System.setProperty("fxembed.build.timestamp", VersionInfoUtil.getBuildTimestamp());
		setLayout(new BorderLayout());
		enableEvents(
				InputEvent.COMPONENT_EVENT_MASK
				| InputEvent.MOUSE_WHEEL_EVENT_MASK
				| AWTEvent.HIERARCHY_EVENT_MASK);
		add(new JLabel("Initializing ...", JLabel.CENTER), BorderLayout.CENTER);
		logger.info("[FXEmbed] finished initializing component");
	}
	
	@Override
	protected void processMouseWheelEvent(MouseWheelEvent e) {
		forwardMouseWheel(e);
		super.processMouseWheelEvent(e);
	}
	
	@Override
	protected void processHierarchyEvent(HierarchyEvent e) {
		updateVisible();
		super.processHierarchyEvent(e);
	}
	
	@Override
	protected void processComponentEvent(ComponentEvent e) {
		if( ComponentEvent.COMPONENT_RESIZED == e.getID()
				|| ComponentEvent.COMPONENT_MOVED == e.getID())  {
			if (windowExists()) {
				resizeWindow(0);
			}			
		}
		super.processComponentEvent(e);
	}

	private void forwardMouseWheel(MouseWheelEvent e) {
		TKSceneListener tkSceneListener = getTKSceneListener();
		double unitsToScrollX = 0;
		double unitsToScrollY = -e.getWheelRotation();
		double totalScrollX = 0;
		double totalScrollY = 0;
		double xMultiplier = 40.0;
		double yMultiplier = 40.0;
		int touchCount = 0;
		int scrollTextX = 0;
		int scrollTextY = 0;
		int defaultTextX = 0;
		int defaultTextY = 0;
// We can send Double.NaN because Scene.ScenePeerListener will then use the cursor position
// and we don't have to fuss around with render-scale who is NOT valid for Swing-APIs on Java-8	
//		float scaleFactor = scaleFactor(e.getXOnScreen(), e.getYOnScreen());
//		double x = e.getX() / scaleFactor;
//		double y = e.getY() / scaleFactor;
//		double screenX = e.getXOnScreen();
//		double screenY = e.getYOnScreen();
		double x = Double.NaN; 
		double y = Double.NaN;
		double screenX = Double.NaN;
		double screenY = Double.NaN;
		boolean _shiftDown = e.isShiftDown();
		boolean _controlDown = e.isControlDown();
		boolean _altDown = e.isAltDown();
		boolean _metaDown = e.isMetaDown();
		boolean _direct = false;
		boolean _inertia = false;
		
		if( tkSceneListener != null ) {
			Platform.runLater( () -> {
				tkSceneListener.scrollEvent(
						ScrollEvent.SCROLL, 
						unitsToScrollX, unitsToScrollY, 
						totalScrollX, totalScrollY, 
						xMultiplier, yMultiplier, 
						touchCount, 
						scrollTextX, scrollTextY, 
						defaultTextX, defaultTextY, 
						x, y, 
						screenX, screenY, 
						_shiftDown, _controlDown, _altDown, _metaDown, _direct, _inertia);
			});			
		}
	}
		
	private TKSceneListener getTKSceneListener() {
		if( sceneListener == null ) {
			Scene scene = stage.getScene();
			try {
				TKScene peer = getTkScene(scene);
				Field field = peer.getClass().getSuperclass().getDeclaredField("sceneListener");
				field.setAccessible(true);
				sceneListener = (TKSceneListener) field.get(peer);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				throw new IllegalStateException("Unable to get scene listener. ", e);
			}
		}
		return sceneListener;
	}
	
	private void updateVisible() {
		if( windowExists() ) {
			Platform.runLater( () -> {
				_updateVisible();
			});
		}
	}

	private void _updateVisible() {
		logger.info("[FXEmbed] updateVisible() START");
		if( windowExists() ) {
			logger.info("[FXEmbed] window exists");
			if (isShowing()) {
				logger.info("[FXEmbed] window is showing");
				WindowsNative.ShowWindow(fxHandle, WindowsNative.SW_SHOW);
				// Looks like a problem in FX-Applications
				resizeWindow(1);
				resizeWindow(0);
			} else {
				logger.info("[FXEmbed] window is not showing");
				WindowsNative.ShowWindow(fxHandle, WindowsNative.SW_HIDE);
			}	
		} else {
			logger.info("[FXEmbed] window does not exist");
		}
		logger.info("[FXEmbed] updateVisible() FINISH");
	}

	private static void bootstrapFX() throws InterruptedException {
		logger.info("[FXEmbed] bootstrapping FX...");
		if (!FX_LAUNCHED) {
			FX_LAUNCHED = true;
			Thread t = new Thread(() -> {
				Platform.setImplicitExit(false);
				Application.launch(Launcher.class);
			});
			t.start();
		}
		START_LATCH.await();
		logger.info("[FXEmbed] FX boostrapped");
	}

	/**
	 * Create a container with a known window handle (might be running in another
	 * process)
	 * 
	 * @param windowHandle the window handle
	 * @return the container
	 * @throws Exception
	 */
	public static FXEmbed createWithHandle(long windowHandle) throws Exception {
		FXEmbed embedder = new FXEmbed();
		embedder.setFXHandle(null, windowHandle, true);
		return embedder;
	}

	/**
	 * Create a container initializing JavaFX and providing a {@link Scene}
	 * 
	 * @param consumer the consumer to initialize the JavaFX UI (called on the
	 *                 JavaFX-Event-Thread)
	 * @return the container
	 * @throws Exception
	 */
	public static FXEmbed create(Consumer<Scene> consumer) throws Exception {
		FXEmbed embedder = new FXEmbed();
		Thread t = new Thread(() -> {
			try {
				bootstrapFX();
			} catch (InterruptedException e) {
				throw new RuntimeException();
			}
			Platform.runLater(() -> {
				Stage s = new Stage();
				//using UTILITY prevents the stage from showing up in the taskbar
				s.initStyle(StageStyle.UTILITY);
				Scene sc = new Scene(new Group());
				// Make sure the native window is not shown to the user before we reparent it
				s.setOpacity(0);
				s.show();
				long rawHandle = getWindowHandle(s);
				embedder.setFXHandle(s, rawHandle, false);
				consumer.accept(sc);
				embedder.stage = s;
				/*
				 * the scene must be put into the stage only after reparenting,
				 * otherwise popups will appear a few pixels too low (as if a window title bar was there) 
				 */
				s.setScene(sc);
			});
		});
		t.start();

		return embedder;
	}
	
	private static long getWindowHandle(Stage s) {
		try {
			TKStage peer = getTkStage(s);
			Class<?> windowStage = peer.getClass(); // com.sun.javafx.tk.quantum.WindowStage
			Method method = windowStage.getDeclaredMethod("getPlatformWindow");
			method.setAccessible(true);
			com.sun.glass.ui.Window w = (com.sun.glass.ui.Window) method.invoke(peer);
			return w.getNativeHandle();
		} catch (Throwable e) {
			throw new IllegalStateException("Unable to get native window handle. ", e);
		}
	}
	
	private static TKStage getTkStage(Stage s) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method getPeer = null;
		try {
			// java 9 and above
			getPeer = javafx.stage.Window.class.getDeclaredMethod("getPeer");
		} catch (NoSuchMethodException e) {
			try {
				// java 8
				getPeer = javafx.stage.Window.class.getDeclaredMethod("impl_getPeer");
			} catch (Throwable e2) {
				throw new IllegalStateException("Unable to get peer. ", e2);
			}
		}
		getPeer.setAccessible(true);
		return (TKStage) getPeer.invoke(s);
	}
	

	private TKScene getTkScene(Scene s) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method getPeer = null;
		try {
			// java 9 and above
			getPeer = Scene.class.getDeclaredMethod("getPeer");
		} catch (NoSuchMethodException e) {
			try {
				// java 8
				getPeer = Scene.class.getDeclaredMethod("impl_getPeer");
			} catch (Throwable e2) {
				throw new IllegalStateException("Unable to get peer. ", e2);
			}
		}
		getPeer.setAccessible(true);
		return (TKScene) getPeer.invoke(s);
	}

	/**
	 * Dispose the component releasing the native resources
	 */
	public void dispose() {
		logger.info("[FXEmbed] disposing...");
		if (stage != null) {
			this.fxHandle = 0;
			
			Window window = SwingUtilities.getWindowAncestor(this);
			stage.close();
			if (window != null) {
				List<Stage> list = STAGES.get(window);
				if (list != null) {
					list.remove(stage);
					window.removeComponentListener(componentListener);
				}
			}
			getParent().remove(this);
			
			stage = null;
			sceneListener = null;
		} else {
			if( windowExists() ) {
				WindowsNative.DestroyWindow(fxHandle);
				this.fxHandle = 0;
			}
		}
		logger.info("[FXEmbed] disposed");
	}
	
	private long getWindowHandle(Object peer) {
		try {
			Class<?> cl = Class.forName("sun.awt.windows.WComponentPeer");
			Method method = cl.getMethod("getHWnd");
			return (long) method.invoke(peer);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void setFXHandle(Stage stage, long handle, boolean externalProcess) {
		SwingUtilities.invokeLater(() -> {
			this.fxHandle = handle;
			Window window = SwingUtilities.getWindowAncestor(this);
			if (window == null) {
				// FIXME We need to handle this situation
				return;
			}

			
			if (!window.isDisplayable()) {
				// FIXME We need to handle this situation
				return;
			}
			
			window.addComponentListener(componentListener);

			Object peer = getAwtWindowPeer(window);
			long hWnd = getWindowHandle(peer);
			long style = WindowsNative.GetWindowLongPtrW(handle,  WindowsNative.GWL_STYLE);
			style = (style & ~(WindowsNative.WS_POPUP) & ~(WindowsNative.WS_CAPTION) & ~(WindowsNative.WS_THICKFRAME) & ~(WindowsNative.WS_MAXIMIZEBOX) & ~(WindowsNative.WS_MINIMIZEBOX)
					& ~(WindowsNative.WS_SYSMENU) & ~(WindowsNative.WS_OVERLAPPED));
			WindowsNative.SetWindowLongPtrW(handle, WindowsNative.GWL_STYLE, style);
			WindowsNative.SetParent(handle, hWnd);
			desktopPositionChanged();

			List<Stage> list = STAGES.get(window);
			if (list == null) {
				list = new ArrayList<>();
				window.addWindowListener(new WindowAdapterImpl(window));
				STAGES.put(window, list);
			}
			list.add(stage);

			if( ! externalProcess ) {
				Platform.runLater(() -> {
					if (stage != null) {
						stage.setOpacity(1);
					}
				});	
			}

			if (!isShowing()) {
				updateVisible();
			} else {
				logger.info("[FXEmbed] Window not showing");
			}
			resizeWindow(0);
			revalidate();
		});
	}
	
	private Object getAwtWindowPeer(Window window) {
		try {
			Field componentPeer = Component.class.getDeclaredField("peer");
			componentPeer.setAccessible(true);
			return componentPeer.get(window);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	void desktopPositionChanged() {
		logger.info("[FXEmbed] desktopPositionChanged...");
		if( windowExists() ) {
			logger.info("[FXEmbed] window exists, sending WM_MOVE message...");
			WindowsNative.SendMessage(fxHandle, WindowsNative.WM_MOVE, 0, 0);	
		}
		logger.info("[FXEmbed] desktopPositionChanged finished");
	}
	
	private void resizeWindow(int delta) {
		logger.info("[FXEmbed] resizeWindow...");
		if( windowExists() ) {
			logger.info("[FXEmbed] window exists, going to call setWindowPos");
			Container parent = findRoot(this);
			Point p = SwingUtilities.convertPoint(this, new Point(0, 0), parent);
			Rectangle b = getBounds();
			int x = p.x;
			int y = p.y;
			int width = b.width - delta;
			int height = b.height;
			logger.info("[FXEmebd] resizing to: " + x + "/" + y + "/" + width + "/" + height);
			
			int flags = WindowsNative.SWP_NOZORDER | WindowsNative.SWP_DRAWFRAME | WindowsNative.SWP_NOACTIVATE | WindowsNative.SWP_ASYNCWINDOWPOS;
			WindowsNative.SetWindowPos(fxHandle, 0, x, y, width, height, flags);
			desktopPositionChanged();	
		}
		logger.info("[FXEmbed] resizeWindow finished");
	}
	
	public Dimension getPreferredSize() {
		logger.info("[FXEmbed] getPreferredSize...");
		final Dimension prefSize;
		if (isPreferredSizeSet() || fxHandle == 0) {
            prefSize = super.getPreferredSize();
        } else {
        	prefSize = runSync( () -> {
            	double w = stage.getScene().getRoot().prefWidth(getBounds().getHeight());
                double h = stage.getScene().getRoot().prefHeight(getBounds().getWidth());
                return new Dimension((int)w, (int)h);	
            });
        }
		logger.info("[FXEmbed] getPreferredSize returns {}X{}", prefSize.width, prefSize.height);
		return prefSize;
    }

	private static <T> T runSync( Supplier<T> s) {
		AtomicReference<T> r = new AtomicReference<>();
		runSync( () -> {
			r.set(s.get());
		});
		return r.get();
	}
	
	public static void runSync(Runnable r) {
		if( FX_LAUNCHED ) {
			CountDownLatch l = new CountDownLatch(1);
			Platform.runLater( () -> {
				r.run();
	        	l.countDown();
	        });
			try {
				l.await(5000,TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				
			}	
		}
		
	}
	
	private static Container findRoot(Container c) {
		while (!(c.getParent() instanceof JFrame)) {
			c = c.getParent();
		}
		return c;
	}
	
	private boolean windowExists() {
		return fxHandle != 0 && WindowsNative.IsWindow(fxHandle) != 0;
	}

	@Deprecated
	public static class Launcher extends Application {
		@Override
		public void start(Stage arg0) throws Exception {
			FXEmbed.START_LATCH.countDown();
		}
	}
	
	static class WindowAdapterImpl extends WindowAdapter {
		private final Window window;

		public WindowAdapterImpl(Window window) {
			this.window = window;
		}

		public void windowClosing(WindowEvent e) {
			logger.info("[FXEmbed] WindowAdapter closing");
			runSync( () -> {
				STAGES.get(window).forEach(Stage::hide);
			});
		}
	}
}
