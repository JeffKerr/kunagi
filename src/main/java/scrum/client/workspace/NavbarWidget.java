/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package scrum.client.workspace;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import scrum.client.admin.LogoutAction;
import scrum.client.common.AScrumWidget;
import scrum.client.project.Project;
import scrum.client.search.SearchInputWidget;
import scrum.client.workspace.WidgetBuilder.DropdownMenu;
import scrum.client.workspace.WidgetBuilder.Li;
import scrum.client.workspace.WidgetBuilder.Nav;
import scrum.client.workspace.WidgetBuilder.Ul;

public class NavbarWidget extends AScrumWidget {

	private Project currentProject;

	@Override
	protected Widget onInitialization() {
		currentProject = getCurrentProjectOrNull();

		Nav nav = new WidgetBuilder().styles("navbar", "navbar-dark", "navbar-fixed-top", "bg-inverse")
				.attr("role", "navigation").createNav();

		nav.add(createTogglerButton());
		nav.add(createBrand());

		if (currentProject != null) {
			nav.add(createNavUser());
			nav.add(createCollapse());
		}

		return nav;
	}

	@Override
	protected boolean isResetRequired() {
		return currentProject != getCurrentProjectOrNull();
	}

	private Widget createCollapse() {
		FlowPanel ret = new WidgetBuilder().styles("collapse", "navbar-toggleable-sm").div();
		ret.add(createNavLinks());
		ret.add(createNavGlobal());
		return ret;
	}

	private Widget createNavUser() {
		Ul ul = new WidgetBuilder().styles("nav navbar-nav pull-xs-right m-l-1").ul();

		DropdownMenu dropdown = new WidgetBuilder().styles("dropdown-menu-right").dropdownMenu();
		String user = getCurrentUser().getName();
		String project = currentProject.getLabel();
		ul.add(dropdown.createWrapperLiNavLink(user + " @ " + project));

		dropdown.addItemA(new CloseProjectAction());

		dropdown.addDivider();

		dropdown.addItemA(Navigator.getPageHref("ProjectUserConfig"), "Personal Settings");
		dropdown.addItemA(Navigator.getPageHref("ProjectAdmin"), "Project Settings");
		dropdown.addItemA(Navigator.getPageHref("SystemConfig"), "Kunagi Settings");
		dropdown.addItemA(Navigator.getPageHref("SystemMessageManager"), "System Message");
		dropdown.addItemA(Navigator.getPageHref("UserList"), "User Management");
		dropdown.addItemA("admin.html", "Web Application Monitoring", true);

		dropdown.addDivider();

		dropdown.addItemA("http://kunagi.org/support.html", "Feedback & Support", true);

		dropdown.addDivider();

		dropdown.addItemA(new LogoutAction());

		return ul;
	}

	private Widget createNavGlobal() {
		Ul ul = new WidgetBuilder().styles("nav navbar-nav pull-xs-right").ul();

		ul.add(createNavGlobalSearch());
		// ul.add(createNavGlobalCommunicationIndicator());

		return ul;
	}

	private Li createNavGlobalCommunicationIndicator() {
		Li li = new WidgetBuilder().styles("nav-item", "pull-xs-none", "pull-md-left").li();

		li.add(new CommunicationIndicatorWidget());

		return li;
	}

	private Li createNavGlobalSearch() {
		Li li = new WidgetBuilder().styles("nav-item", "pull-xs-none", "pull-md-left").li();

		// FlowPanel input = new WidgetBuilder().styles("form-control").attr("type", "text")
		// .attr("placeholder", "Search...").input();
		SearchInputWidget inputWidget = new SearchInputWidget();
		li.add(inputWidget);

		return li;
	}

	private Widget createNavLinks() {
		Ul ul = new WidgetBuilder().styles("nav navbar-nav pull-xs-left").ul();

		ul.addNavItemA(Navigator.getPageHref("Dashboard"), "Dashboard");
		ul.addNavItemA(Navigator.getPageHref("Whiteboard"), "Sprint");
		ul.add(createNavLinksProduct());
		ul.add(createNavLinksProject());
		ul.add(createNavLinksCollaboration());

		return ul;
	}

	private Li createNavLinksProduct() {
		DropdownMenu dropdownMenu = new WidgetBuilder().dropdownMenu();

		dropdownMenu.addItemA(Navigator.getPageHref("ProductBacklog"), "Product Backlog");
		dropdownMenu.addItemA(Navigator.getPageHref("IssueManagement"), "Issues");
		dropdownMenu.addItemA(Navigator.getPageHref("ReleaseManagement"), "Releases");
		dropdownMenu.addItemA(Navigator.getPageHref("QualityBacklog"), "Qualities");
		dropdownMenu.addItemA(Navigator.getPageHref("Blog"), "Blog");

		return dropdownMenu.createWrapperLiNavLink("Product");
	}

	private Li createNavLinksProject() {
		DropdownMenu dropdownMenu = new WidgetBuilder().dropdownMenu();

		dropdownMenu.addItemA(Navigator.getPageHref("ImpedimentList"), "Impediments");
		dropdownMenu.addItemA(Navigator.getPageHref("RiskList"), "Risks");
		dropdownMenu.addItemA(Navigator.getPageHref("Journal"), "Journal");
		dropdownMenu.addItemA(Navigator.getPageHref("NextSprint"), "Next Sprint");
		dropdownMenu.addItemA(Navigator.getPageHref("SprintHistory"), "Sprint History");

		return dropdownMenu.createWrapperLiNavLink("Project");
	}

	private Li createNavLinksCollaboration() {
		DropdownMenu dropdownMenu = new WidgetBuilder().dropdownMenu();

		dropdownMenu.addItemA(Navigator.getPageHref("Wiki"), "Wiki");
		dropdownMenu.addItemA(Navigator.getPageHref("Forum"), "Discussion");
		dropdownMenu.addItemA(Navigator.getPageHref("Calendar"), "Calendar");
		dropdownMenu.addItemA(Navigator.getPageHref("FileRepository"), "Files");
		dropdownMenu.addItemA(Navigator.getPageHref("Punishments"), "Courtroom");

		return dropdownMenu.createWrapperLiNavLink("Collaboration");
	}

	private Widget createBrand() {
		return new WidgetBuilder().styles("navbar-brand", "hidden-sm-down").a("#", "Kunagi");
	}

	private Widget createTogglerButton() {
		Button togglerButton = new WidgetBuilder().styles("navbar-toggler", "hidden-md-up")
				.attr("data-toggle", "collapse").attr("data-target", ".navbar-toggleable-sm").button("Menu");
		togglerButton.setHTML("&#9776;"); // 3bars
		return togglerButton;
	}

}
