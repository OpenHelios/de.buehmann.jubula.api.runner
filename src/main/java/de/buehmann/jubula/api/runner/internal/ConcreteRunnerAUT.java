package de.buehmann.jubula.api.runner.internal;

import org.eclipse.jubula.client.AUT;
import org.eclipse.jubula.toolkit.base.components.GraphicsComponent;
import org.eclipse.jubula.toolkit.base.components.TextComponent;
import org.eclipse.jubula.toolkit.base.components.TextInputComponent;
import org.eclipse.jubula.toolkit.concrete.components.TabComponent;
import org.eclipse.jubula.toolkit.enums.ValueSets.InteractionMode;
import org.eclipse.jubula.toolkit.enums.ValueSets.Modifier;
import org.eclipse.jubula.toolkit.enums.ValueSets.Operator;
import org.eclipse.jubula.toolkit.enums.ValueSets.Unit;

import de.buehmann.jubula.api.runner.RunnerAUT;

public abstract class ConcreteRunnerAUT implements RunnerAUT {

	private AUT aut;

	@Override
	public void setAUT(final AUT aut) {
		if (null != this.aut) {
			this.aut.disconnect();
		}
		this.aut = aut;
		aut.connect();
	}

	@Override
	public AUT getAUT() {
		return aut;
	}

	public void assertEnabled(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkEnablement(true), "enabled");
	}

	public void assertNotEnabled(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkEnablement(false), "not enabled");
	}

	public void assertExists(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkExistence(true), "exists");
	}

	public void assertNotExists(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkExistence(false), "not exists");
	}

	public void assertFocused(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkFocus(true), "input text focused");
	}

	public void assertNotFocused(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkFocus(false), "input text focused");
	}

	public void assertPropertyEquals(final String propertyName, final String expectedValue,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkProperty(propertyName, expectedValue, Operator.equals),
				"input text property equals");
	}

	public void assertPropertyNotEquals(final String propertyName, final String expectedValue,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkProperty(propertyName, expectedValue, Operator.notEquals),
				"input text property not equals");
	}

	public void assertPropertyMatches(final String propertyName, final String expectedValue,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkProperty(propertyName, expectedValue, Operator.matches),
				"input text property matches");
	}

	public void assertPropertySimpleMatch(final String propertyName, final String expectedValue,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.checkProperty(propertyName, expectedValue, Operator.simpleMatch),
				"input text property simple match");
	}

	public void click(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.click(1, InteractionMode.primary), "click");
	}

	public void clickDouble(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.click(2, InteractionMode.primary), "double click");
	}

	public void clickRight(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.click(1, InteractionMode.tertiary), "right click");
	}

	public void clickMiddle(final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.click(1, InteractionMode.secondary), "middle click");
	}

	public void clickPosition(final InteractionMode button, final int clickCount, final int xFromLeftInPercent,
			final int yFromTopInPercent, final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.clickInComponent(clickCount, button, xFromLeftInPercent, Unit.percent,
				yFromTopInPercent, Unit.percent), "click at position in component");
	}

	public void dragBegin(final int xFromLeftInPercent, final int yFromTopInPercent,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.drag(InteractionMode.primary, new Modifier[0], xFromLeftInPercent,
				Unit.percent, yFromTopInPercent, Unit.percent), "drag begin");
	}

	public void dropEnd(final int xFromLeftInPercent, final int yFromTopInPercent,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.drop(xFromLeftInPercent, Unit.percent, yFromTopInPercent, Unit.percent, 200),
				"drop end");
	}

	public void waitForComponent(final int timeoutInMs, final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.waitForComponent(timeoutInMs, 100), "wait for component");
	}

	public void waitForComponentWithDelay(final int timeoutInMs, final int delayAfterVisibilityInMs,
			final GraphicsComponent graphicsComponent) {
		getAUT().execute(graphicsComponent.waitForComponent(timeoutInMs, delayAfterVisibilityInMs),
				"wait for component with delay");
	}

	// text components
	public void assertTextEquals(final String expectedText, final TextComponent textComponent) {
		getAUT().execute(textComponent.checkText(expectedText, Operator.equals), "text equals");
	}

	public void assertTextNotEquals(final String expectedText, final TextComponent textComponent) {
		getAUT().execute(textComponent.checkText(expectedText, Operator.notEquals), "text not equals");
	}

	public void assertTextMatches(final String expectedText, final TextComponent textComponent) {
		getAUT().execute(textComponent.checkText(expectedText, Operator.matches), "text matches");
	}

	public void assertTextSimpleMatch(final String expectedText, final TextComponent textComponent) {
		getAUT().execute(textComponent.checkText(expectedText, Operator.simpleMatch), "input text simple match");
	}

	// input text components

	public void assertEditable(final TextInputComponent textInput) {
		getAUT().execute(textInput.checkEditability(true), "input text editable");
	}

	public void assertNotEditable(final TextInputComponent textInput) {
		getAUT().execute(textInput.checkEditability(false), "input text not editable");
	}

	public void inputText(final String text, final TextInputComponent textInput) {
		getAUT().execute(textInput.inputText(text), "input text");
	}

	public void replaceText(final String text, final TextInputComponent textInput) {
		getAUT().execute(textInput.replaceText(text), "replace text");
	}

	public void insertTextAfterIndex(final String text, final int index, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextAfterIndex(text, index), "insert text");
	}

	public void insertTextAfter(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.equals, true), "insert text after");
	}

	public void insertTextAfterSimpleMatch(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.simpleMatch, true), "insert text after simple match");
	}

	public void insertTextAfterMatches(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.matches, true), "insert text after matches");
	}

	public void insertTextBefore(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.equals, false), "insert text before");
	}

	public void insertTextBeforeSimpleMatch(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.simpleMatch, false), "insert text after simple match");
	}

	public void insertTextBeforeMatches(final String text, final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.insertTextBeforeAfterPattern(text, pattern, Operator.matches, false), "insert text after matches");
	}

	public void selectAll(final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.selectAll(), "select all");
	}

	public void selectText(final String text, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.selectPattern(text, Operator.equals), "select text");
	}

	public void selectTextNotEquals(final String text, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.selectPattern(text, Operator.notEquals), "select text not equals");
	}

	public void selectTextSimpleMatch(final String text, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.selectPattern(text, Operator.simpleMatch), "select text simple match");
	}

	public void selectTextMatches(final String pattern, final org.eclipse.jubula.toolkit.concrete.components.TextInputComponent textInput) {
		getAUT().execute(textInput.selectPattern(pattern, Operator.matches), "select text matches");
	}

	// tab component

	public void selectTabByIndex(final int index, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.selectTabByIndex(index), "select text matches");
	}

	public void selectTabByTitle(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.selectTabByValue(title, Operator.equals), "select tab by title");
	}

	public void selectTabByTitleSimpleMatch(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.selectTabByValue(title, Operator.simpleMatch), "select tab by title");
	}

	public void selectTabByTitleMatches(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.selectTabByValue(title, Operator.matches), "select tab by title");
	}

	public void assertTabSelectedByIndex(final int index, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByIndex(index, true), "select text matches");
	}

	public void assertTabSelectedByTitle(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.equals, true), "select tab by title");
	}

	public void assertTabSelectedByTitleSimpleMatch(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.simpleMatch, true), "select tab by title");
	}

	public void assertTabSelectedByTitleMatches(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.matches, true), "select tab by title");
	}

	public void assertTabNotSelectedByIndex(final int index, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByIndex(index, false), "select text matches");
	}

	public void assertTabNotSelectedByTitle(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.equals, false), "select tab by title");
	}

	public void assertTabNotSelectedByTitleSimpleMatch(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.simpleMatch, false), "select tab by title");
	}

	public void assertTabNotSelectedByTitleMatches(final String title, final TabComponent tabComponent) {
		getAUT().execute(tabComponent.checkSelectionOfTabByValue(title, Operator.matches, false), "select tab by title");
	}

}
