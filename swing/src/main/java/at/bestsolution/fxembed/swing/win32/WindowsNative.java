/**
 * Copyright (C) 2020 - BestSolution.at
 */
package at.bestsolution.fxembed.swing.win32;

@SuppressWarnings("javadoc")
public class WindowsNative {
    
	static {
		System.loadLibrary("WindowsNative");
	}
	
    // winuser.h
    // HWND SetParent(HWND hWndChild, HWND hWndNewParent) 
    public static native long SetParent(long hWndChild, long hWndNewParent);
    
    // winuser.h
    // LRESULT SendMessage(HWND hWnd, UINT Msg, WPARAM wParam, LPARAM lParam)
    public static native long SendMessage(long hWnd, int msg, long wParam, long lParam);
    
    // winuser.h
    // BOOL SetWindowPos(HWND hWnd, HWND hWndInsertAfter, int X, int Y, int cx, int cy, UINT uFlags)
    public static native boolean SetWindowPos(long hWnd, long hWndInsertAfter, int x, int y, int cx, int cy, int uFlags);
    
    // winuser.h
    // BOOL DestroyWindow(HWND hWnd)
    public static native boolean DestroyWindow(long hWnd);
    
    
    // winuser.h
    // BOOL ShowWindow(HWND hWnd, int nCmdShow)
    public static native boolean ShowWindow(long hWnd, int nCmdShow);
    
    
    public static native long GetWindowLongPtrW(long hWnd, int nIndex);
    public static native long SetWindowLongPtrW(long hWnd, int nIndex, long dwNewLong);
    
    
    public static final int WM_ACTIVATE = 0x6;
    public static final int WM_ACTIVATEAPP = 0x1c;
    public static final int WM_APP = 0x8000;
    public static final int WM_DWMCOLORIZATIONCOLORCHANGED = 0x320;
    public static final int WM_CANCELMODE = 0x1f;
    public static final int WM_CAPTURECHANGED = 0x0215;
    public static final int WM_CHANGEUISTATE = 0x0127;
    public static final int WM_CHAR = 0x102;
    public static final int WM_CLEAR = 0x303;
    public static final int WM_CLOSE = 0x10;
    public static final int WM_COMMAND = 0x111;
    public static final int WM_CONTEXTMENU = 0x7b;
    public static final int WM_COPY = 0x301;
    public static final int WM_CREATE = 0x0001;
    public static final int WM_CTLCOLORBTN = 0x135;
    public static final int WM_CTLCOLORDLG = 0x136;
    public static final int WM_CTLCOLOREDIT = 0x133;
    public static final int WM_CTLCOLORLISTBOX = 0x134;
    public static final int WM_CTLCOLORMSGBOX = 0x132;
    public static final int WM_CTLCOLORSCROLLBAR = 0x137;
    public static final int WM_CTLCOLORSTATIC = 0x138;
    public static final int WM_CUT = 0x300;
    public static final int WM_DEADCHAR = 0x103;
    public static final int WM_DESTROY = 0x2;
    public static final int WM_DPICHANGED = 0x02E0;
    public static final int WM_DRAWITEM = 0x2b;
    public static final int WM_ENDSESSION = 0x16;
    public static final int WM_ENTERIDLE = 0x121;
    public static final int WM_ERASEBKGND = 0x14;
    public static final int WM_GESTURE = 0x0119;
    public static final int WM_GETDLGCODE = 0x87;
    public static final int WM_GETFONT = 0x31;
    public static final int WM_GETOBJECT = 0x003D;
    public static final int WM_GETMINMAXINFO = 0x0024;
    public static final int WM_HELP = 0x53;
    public static final int WM_HOTKEY = 0x0312;
    public static final int WM_HSCROLL = 0x114;
    public static final int WM_IME_CHAR = 0x286;
    public static final int WM_IME_COMPOSITION = 0x10f;
    public static final int WM_IME_COMPOSITION_START = 0x010D;
    public static final int WM_IME_ENDCOMPOSITION = 0x010E;
    public static final int WM_INITDIALOG = 0x110;
    public static final int WM_INITMENUPOPUP = 0x117;
    public static final int WM_INPUTLANGCHANGE = 0x51;
    public static final int WM_KEYDOWN = 0x100;
    public static final int WM_KEYFIRST = 0x100;
    public static final int WM_KEYLAST = 0x108;
    public static final int WM_KEYUP = 0x101;
    public static final int WM_KILLFOCUS = 0x8;
    public static final int WM_LBUTTONDBLCLK = 0x203;
    public static final int WM_LBUTTONDOWN = 0x201;
    public static final int WM_LBUTTONUP = 0x202;
    public static final int WM_MBUTTONDBLCLK = 0x209;
    public static final int WM_MBUTTONDOWN = 0x207;
    public static final int WM_MBUTTONUP = 0x208;
    public static final int WM_MEASUREITEM = 0x2c;
    public static final int WM_MENUCHAR = 0x120;
    public static final int WM_MENUSELECT = 0x11f;
    public static final int WM_MOUSEACTIVATE = 0x21;
    public static final int WM_MOUSEFIRST = 0x200;
    public static final int WM_MOUSEHOVER = 0x2a1;
    public static final int WM_MOUSELEAVE = 0x2a3;
    public static final int WM_MOUSEMOVE = 0x200;
    public static final int WM_MOUSEWHEEL = 0x20a;
    public static final int WM_MOUSEHWHEEL = 0x20e;
    public static final int WM_MOUSELAST = 0x20d;
    public static final int WM_MOVE = 0x3;
    public static final int WM_NCACTIVATE = 0x86;
    public static final int WM_NCCALCSIZE = 0x83;
    public static final int WM_NCHITTEST = 0x84;
    public static final int WM_NCLBUTTONDOWN = 0x00A1;
    public static final int WM_NCPAINT = 0x85;
    public static final int WM_NOTIFY = 0x4e;
    public static final int WM_NULL = 0x0;
    public static final int WM_PAINT = 0xf;
    public static final int WM_PARENTNOTIFY = 0x0210;
    public static final int WM_ENTERMENULOOP = 0x0211;
    public static final int WM_EXITMENULOOP = 0x0212;
    public static final int WM_ENTERSIZEMOVE = 0x0231;
    public static final int WM_EXITSIZEMOVE = 0x0232;
    public static final int WM_PASTE = 0x302;
    public static final int WM_PRINT = 0x0317;
    public static final int WM_PRINTCLIENT = 0x0318;
    public static final int WM_QUERYENDSESSION = 0x11;
    public static final int WM_QUERYOPEN = 0x13;
    public static final int WM_QUERYUISTATE = 0x129;
    public static final int WM_RBUTTONDBLCLK = 0x206;
    public static final int WM_RBUTTONDOWN = 0x204;
    public static final int WM_RBUTTONUP = 0x205;
    public static final int WM_SETCURSOR = 0x20;
    public static final int WM_SETFOCUS = 0x7;
    public static final int WM_SETFONT = 0x30;
    public static final int WM_SETICON = 0x80;
    public static final int WM_SETREDRAW = 0xb;
    public static final int WM_SETTEXT = 12;
    public static final int WM_SETTINGCHANGE = 0x1A;
    public static final int WM_SHOWWINDOW = 0x18;
    public static final int WM_SIZE = 0x5;
    public static final int WM_SYSCHAR = 0x106;
    public static final int WM_SYSCOLORCHANGE = 0x15;
    public static final int WM_SYSCOMMAND = 0x112;
    public static final int WM_SYSKEYDOWN = 0x104;
    public static final int WM_SYSKEYUP = 0x105;
    public static final int WM_TABLET_FLICK = 0x02C0 + 11;
    public static final int WM_TIMER = 0x113;
    public static final int WM_THEMECHANGED = 0x031a;
    public static final int WM_TOUCH = 0x240;
    public static final int WM_UNDO = 0x304;
    public static final int WM_UNINITMENUPOPUP = 0x0125;
    public static final int WM_UPDATEUISTATE = 0x0128;
    public static final int WM_USER = 0x400;
    public static final int WM_VSCROLL = 0x115;
    public static final int WM_WINDOWPOSCHANGED = 0x47;
    public static final int WM_WINDOWPOSCHANGING = 0x46;
    
    public static final int SWP_ASYNCWINDOWPOS = 0x4000;
    public static final int SWP_DRAWFRAME = 0x20;
    public static final int SWP_NOACTIVATE = 0x10;
    public static final int SWP_NOCOPYBITS = 0x100;
    public static final int SWP_NOMOVE = 0x2;
    public static final int SWP_NOREDRAW = 0x8;
    public static final int SWP_NOSIZE = 0x1;
    public static final int SWP_NOZORDER = 0x4;
    
    public static final int SW_ERASE = 0x4;
	public static final int SW_HIDE = 0x0;
	public static final int SW_INVALIDATE = 0x2;
	public static final int SW_MINIMIZE = 0x6;
	public static final int SW_PARENTOPENING = 0x3;
	public static final int SW_RESTORE = 0x9;
	public static final int SW_SCROLLCHILDREN = 0x1;
	public static final int SW_SHOW = 0x5;
	public static final int SW_SHOWMAXIMIZED = 0x3;
	public static final int SW_SHOWMINIMIZED = 0x2;
	public static final int SW_SHOWMINNOACTIVE = 0x7;
	public static final int SW_SHOWNA = 0x8;
	public static final int SW_SHOWNOACTIVATE = 0x4;
	
	public static final int GWL_WNDPROC =         (-4);
	public static final int GWL_HINSTANCE =       (-6);
	public static final int GWL_HWNDPARENT =      (-8);
	public static final int GWL_STYLE =           (-16);
	public static final int GWL_EXSTYLE =         (-20);
	public static final int GWL_USERDATA =        (-21);
	public static final int GWL_ID =              (-12);
    
	
	public static final long  WS_OVERLAPPED     =  0x00000000L;
	public static final long  WS_POPUP         =   0x80000000L;
	public static final long  WS_CHILD         =   0x40000000L;
	public static final long  WS_MINIMIZE     =    0x20000000L;
	public static final long  WS_VISIBLE      =    0x10000000L;
	public static final long  WS_DISABLED      =   0x08000000L;
	public static final long  WS_CLIPSIBLINGS  =   0x04000000L;
	public static final long  WS_CLIPCHILDREN  =   0x02000000L;
	public static final long  WS_MAXIMIZE      =   0x01000000L;
	public static final long  WS_CAPTION       =   0x00C00000L;     /* WS_BORDER | WS_DLGFRAME  */
	public static final long  WS_BORDER       =    0x00800000L;
	public static final long  WS_DLGFRAME      =   0x00400000L;
	public static final long  WS_VSCROLL       =   0x00200000L;
	public static final long  WS_HSCROLL       =   0x00100000L;
	public static final long  WS_SYSMENU       =   0x00080000L;
	public static final long  WS_THICKFRAME    =   0x00040000L;
	public static final long  WS_GROUP         =   0x00020000L;
	public static final long  WS_TABSTOP        =  0x00010000L;

	public static final long  WS_MINIMIZEBOX  =    0x00020000L;
	public static final long  WS_MAXIMIZEBOX  =    0x00010000L;
	
}
