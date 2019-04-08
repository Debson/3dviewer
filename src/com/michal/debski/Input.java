/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Input class manages keyboard and mouse input from the user.
 *
 */

package com.michal.debski;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

// Enum that stores most of the possible keystrokes
enum Keycode
{
	None(GLFW_KEY_UNKNOWN),

	Return(GLFW_KEY_ENTER),
	Escape(GLFW_KEY_ESCAPE),
	Backspace(GLFW_KEY_BACKSPACE),
	Tab(GLFW_KEY_TAB),
	Space(GLFW_KEY_SPACE),
	Comma(GLFW_KEY_COMMA),
	Minus(GLFW_KEY_MINUS),
	Period(GLFW_KEY_PERIOD),
	Slash(GLFW_KEY_SLASH),
	D0(GLFW_KEY_0),
	D1(GLFW_KEY_1),
	D2(GLFW_KEY_2),
	D3(GLFW_KEY_3),
	D4(GLFW_KEY_4),
	D5(GLFW_KEY_5),
	D6(GLFW_KEY_6),
	D7(GLFW_KEY_7),
	D8(GLFW_KEY_8),
	D9(GLFW_KEY_9),
	Semicolon(GLFW_KEY_SEMICOLON),
	Apostrophe(GLFW_KEY_APOSTROPHE),
	Equals(GLFW_KEY_EQUAL),
	LeftBracket(GLFW_KEY_LEFT_BRACKET),
	Backslash(GLFW_KEY_BACKSLASH),
	RightBracket(GLFW_KEY_RIGHT_BRACKET),
	Grave(GLFW_KEY_GRAVE_ACCENT),
	A(GLFW_KEY_A),
	B(GLFW_KEY_B),
	C(GLFW_KEY_C),
	D(GLFW_KEY_D),
	E(GLFW_KEY_E),
	F(GLFW_KEY_F),
	G(GLFW_KEY_G),
	H(GLFW_KEY_H),
	I(GLFW_KEY_I),
	J(GLFW_KEY_J),
	K(GLFW_KEY_K),
	L(GLFW_KEY_L),
	M(GLFW_KEY_M),
	N(GLFW_KEY_N),
	O(GLFW_KEY_O),
	P(GLFW_KEY_P),
	Q(GLFW_KEY_Q),
	R(GLFW_KEY_R),
	S(GLFW_KEY_S),
	T(GLFW_KEY_T),
	U(GLFW_KEY_U),
	V(GLFW_KEY_V),
	W(GLFW_KEY_W),
	X(GLFW_KEY_X),
	Y(GLFW_KEY_Y),
	Z(GLFW_KEY_Z),

	Capslock(GLFW_KEY_CAPS_LOCK),

	F1(GLFW_KEY_F1),
	F2(GLFW_KEY_F2),
	F3(GLFW_KEY_F3),
	F4(GLFW_KEY_F4),
	F5(GLFW_KEY_F5),
	F6(GLFW_KEY_F6),
	F7(GLFW_KEY_F7),
	F8(GLFW_KEY_F8),
	F9(GLFW_KEY_F9),
	F10(GLFW_KEY_F10),
	F11(GLFW_KEY_F11),
	F12(GLFW_KEY_F12),

	Prs32screen(GLFW_KEY_PRINT_SCREEN),
	Scrolllock(GLFW_KEY_SCROLL_LOCK),
	Pause(GLFW_KEY_PAUSE),
	Insert(GLFW_KEY_INSERT),
	Home(GLFW_KEY_HOME),
	PageUp(GLFW_KEY_PAGE_UP),
	Delete(GLFW_KEY_DELETE),
	End(GLFW_KEY_END),
	PageDown(GLFW_KEY_PAGE_DOWN),
	Right(GLFW_KEY_RIGHT),
	Left(GLFW_KEY_LEFT),
	Down(GLFW_KEY_DOWN),
	Up(GLFW_KEY_UP),

	KeypadDivide(GLFW_KEY_KP_DIVIDE),
	KeypadMultiply(GLFW_KEY_KP_MULTIPLY),
	KeypadMinus(GLFW_KEY_KP_SUBTRACT),
	KeypadPlus(GLFW_KEY_KP_ADD),
	KeypadEnter(GLFW_KEY_KP_ENTER),
	Keypad1(GLFW_KEY_KP_1),
	Keypad2(GLFW_KEY_KP_2),
	Keypad3(GLFW_KEY_KP_3),
	Keypad4(GLFW_KEY_KP_4),
	Keypad5(GLFW_KEY_KP_5),
	Keypad6(GLFW_KEY_KP_6),
	Keypad7(GLFW_KEY_KP_7),
	Keypad8(GLFW_KEY_KP_8),
	Keypad9(GLFW_KEY_KP_9),
	Keypad0(GLFW_KEY_KP_0),
	KeypadPeriod(GLFW_KEY_KP_DECIMAL),

	F13(GLFW_KEY_F13),
	F14(GLFW_KEY_F14),
	F15(GLFW_KEY_F15),
	F16(GLFW_KEY_F16),
	F17(GLFW_KEY_F17),
	F18(GLFW_KEY_F18),
	F19(GLFW_KEY_F19),
	F20(GLFW_KEY_F20),
	F21(GLFW_KEY_F21),
	F22(GLFW_KEY_F22),
	F23(GLFW_KEY_F23),
	F24(GLFW_KEY_F24),
	Menu(GLFW_KEY_MENU),

	LCtrl(GLFW_KEY_LEFT_CONTROL),
	LShift(GLFW_KEY_LEFT_SHIFT),
	LAlt(GLFW_KEY_LEFT_ALT),
	RCtrl(GLFW_KEY_RIGHT_CONTROL),
	RShift(GLFW_KEY_RIGHT_SHIFT),
	RAlt(GLFW_KEY_RIGHT_ALT),

	MouseLeft(GLFW_MOUSE_BUTTON_LEFT),
	MouseRight(GLFW_MOUSE_BUTTON_RIGHT),
	MouseMiddle(GLFW_MOUSE_BUTTON_MIDDLE),

	MouseWheelUp(GLFW_KEY_RIGHT_ALT + 4),
	MouseWheelDown(GLFW_KEY_RIGHT_ALT + 5),
	count(GLFW_KEY_RIGHT_ALT + 6),;

	private final int keycode;

	Keycode(int keycode)
	{
		this.keycode = keycode;
	}

	public int getValue()
	{
		return keycode;
	}

};

public class Input
{
    private static float posX, posY, posXPrev, posYPrev;
    private static boolean first = true;
    private static boolean[] keys = new boolean[Keycode.count.getValue()];
	private static boolean[] prevKeys = new boolean[Keycode.count.getValue()];

    public static boolean IsKeyPressed(Keycode keycode)
    {
		return keys[keycode.getValue()] && !prevKeys[keycode.getValue()];
    }

    public static boolean IsKeyDown(Keycode keycode)
	{
		return keys[keycode.getValue()];
	}

	public static boolean IsKeyReleased(Keycode keycode)
	{
		return !keys[keycode.getValue()] && prevKeys[keycode.getValue()];
	}

    public static void OnStartFrame()
    {
		prevKeys = keys.clone();
    }

    public static void UpdateKeyState(int key, int action)
	{
		keys[key] = action != GLFW_RELEASE;
	}

    public static void CursorPosCallback(double xpos, double ypos)
    {
        if(posX != posXPrev)
            posXPrev = posX;

        if(posY != posYPrev)
            posYPrev = posY;

        posX = (float)xpos;
        posY = (float)ypos;
    }

    static Vector2f GetRelativeMousePos()
    {
    	// First read of the mouse posX will be large, don't process it.
        if(first)
        {
            first = false;
            return new Vector2f(0.f, 0.f);
        }
        return new Vector2f(posX - posXPrev, posY - posYPrev);
    }
}
