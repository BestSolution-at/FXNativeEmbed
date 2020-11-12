/**
 * Copyright (C) 2020 - BestSolution.at
 */
#include <jni.h>
#include <windows.h>


extern "C" JNIEXPORT jlong JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_SetParent
(JNIEnv *env, jobject obj, jlong hWndChild, jlong hWndNewParent) {

    return (jlong)SetParent((HWND)hWndChild, (HWND)hWndNewParent);

}


extern "C" JNIEXPORT jlong JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_SendMessage
(JNIEnv *env, jobject obj, jlong hWnd, jint msg, jlong wParam, jlong lParam) {

    return (jlong)SendMessage((HWND)hWnd, (UINT)msg, (WPARAM)wParam, (LPARAM)lParam);

}

extern "C" JNIEXPORT jboolean JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_SetWindowPos
(JNIEnv *env, jobject obj, jlong hWnd, jlong hWndInsertAfter, jint x, jint y, jint cx, jint cy, jint uFlags) {

    return (jboolean)SetWindowPos((HWND)hWnd, (HWND)hWndInsertAfter, (int)x, (int)y, (int)cx, (int)cy, (UINT)uFlags);

}

extern "C" JNIEXPORT jboolean JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_ShowWindow
(JNIEnv *env, jobject obj, jlong hWnd, jint nCmdShow) {

    return (jboolean)ShowWindow((HWND)hWnd, (int) nCmdShow);

}

extern "C" JNIEXPORT jboolean JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_DestroyWindow
(JNIEnv *env, jobject obj, jlong hWnd) {

    return (jboolean)DestroyWindow((HWND)hWnd);

}

extern "C" JNIEXPORT jlong JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_GetWindowLongPtrW
(JNIEnv *env, jobject obj, jlong hWnd, jint nIndex) {

    return (jlong)GetWindowLongPtrW((HWND)hWnd, (int)nIndex);
}

extern "C" JNIEXPORT jlong JNICALL Java_at_bestsolution_fxembed_swing_win32_WindowsNative_SetWindowLongPtrW
(JNIEnv *env, jobject obj, jlong hWnd, jint nIndex, jlong dwNewLong) {

    return (jlong)SetWindowLongPtrW((HWND)hWnd, (int)nIndex, (LONG_PTR) dwNewLong);
}
