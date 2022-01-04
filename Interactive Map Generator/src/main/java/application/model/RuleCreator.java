package application.model;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.RuleFactory;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.Selector;
import cz.vutbr.web.csskit.RuleFactoryImpl;
import cz.vutbr.web.csskit.TermFactoryImpl;

public class RuleCreator {
//Creates a new rule to add to the css file for a specific US state
	public static RuleSet cssHoverColorRule(USState state, String color) {
		RuleFactory rf = RuleFactoryImpl.getInstance();
		RuleSet rule = rf.createSet();
		rule.unlock();
		Selector stateSelector = rf.createSelector();
		stateSelector.unlock();
		Selector.ElementID id = rf.createID(state.getAbbr());
		Selector.PseudoPage pseudoClass = rf.createPseudoPage("hover");
		stateSelector.add(id);
		stateSelector.add(pseudoClass);
		CombinedSelector comboSelector = rf.createCombinedSelector();
		comboSelector.unlock();
		comboSelector.add(stateSelector);
		Declaration fill = rf.createDeclaration();
		fill.unlock();
		fill.setProperty("fill");
		fill.setImportant(true);
		fill.add(TermFactoryImpl.getInstance().createColor(color));
		List<CombinedSelector> comboSelectList = new ArrayList<CombinedSelector>();
		List<Declaration> declarationsList = new ArrayList<Declaration>();
		comboSelectList.add(comboSelector);
		declarationsList.add(fill);
		rule.replaceAll(declarationsList);
		rule.setSelectors(comboSelectList);
		System.out.println(rule.get(0));
		return rule;
	}
}
