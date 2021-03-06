/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.workbench.pr.client.editors.instance.list;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.DOMTokenList;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.workbench.pr.client.resources.i18n.Constants;
import org.jbpm.workbench.pr.model.ProcessInstanceSummary;

@Dependent
@Templated(value = "ProcessInstanceSummaryErrorPopoverCell.html", stylesheet = "ProcessInstanceSummaryErrorPopoverCell.css")
public class ProcessInstanceSummaryErrorPopoverCell extends AbstractCell<ProcessInstanceSummary> implements IsElement {

    private static final Constants I18N = Constants.INSTANCE;
    private static final String POPOVER_NAME = "pisepc-popover";
    private static final String VIEW_ERROR_LINK_NAME = "pisepc-view-err-anchor";
    private static final String PROCESS_INSTANCE_ATTRIBUTE = "data-jbpm-processInstanceId";
    private static final String ERROR_PRESENT_STYLE = "error-present";
    private static final String LINK_AVAILABLE_STYLE = "link-available";

    @Inject
    @DataField(POPOVER_NAME)
    private Anchor popover;

    @Inject
    @DataField("pisepc-popover-content")
    private Span popoverContent;

    @Inject
    @DataField("pisepc-content-err-count")
    private Span contentErrCount;

    @Inject
    @DataField(VIEW_ERROR_LINK_NAME)
    private Anchor viewErrAnchor;

    private ProcessInstanceListPresenter viewPresenter;

    public ProcessInstanceSummaryErrorPopoverCell init(final ProcessInstanceListPresenter viewPresenter) {
        this.viewPresenter = viewPresenter;
        return this;
    }

    @Override
    public void render(Context context,
                       ProcessInstanceSummary value,
                       SafeHtmlBuilder sb) {
        Integer errCount = (value != null && value.getErrorCount() != null ? value.getErrorCount() : 0);

        DOMTokenList popoverClasses = popover.getClassList();
        popover.setTextContent(Integer.toString(errCount));

        if (errCount > 0) {
            popoverClasses.add(ERROR_PRESENT_STYLE);
        } else {
            popoverClasses.remove(ERROR_PRESENT_STYLE);
        }

        if (viewPresenter.getViewErrorsActionCondition().test(value)) {
            viewErrAnchor.setTitle(I18N.ErrorCountViewLink());
            viewErrAnchor.setTextContent(I18N.ErrorCountViewLink());
            viewErrAnchor.setAttribute(PROCESS_INSTANCE_ATTRIBUTE,
                                       Long.toString(value.getProcessInstanceId()));
            contentErrCount.setTextContent(I18N.ErrorCountNumber(errCount));

            popoverClasses.add(LINK_AVAILABLE_STYLE);
            popover.setAttribute("data-toggle",
                                 "popover");
            Scheduler.get().scheduleDeferred(() -> initPopovers(POPOVER_NAME,
                                                                VIEW_ERROR_LINK_NAME,
                                                                PROCESS_INSTANCE_ATTRIBUTE));
        } else {
            popover.removeAttribute("data-toggle");
            popoverClasses.remove(LINK_AVAILABLE_STYLE);
        }

        sb.appendHtmlConstant(popover.getOuterHTML());
    }

    public void openErrorView(final String pid) {
        viewPresenter.openErrorView(pid);
    }

    public String getPopoverContent() {
        return popoverContent.getInnerHTML();
    }

    private native void initPopovers(String popoverName,
                                     String linkName,
                                     String procIdAttrName) /*-{
        var thisCellRef = this;
        $wnd.jQuery(document).ready(function () {
            $wnd.jQuery("[data-field='" + popoverName + "'][data-toggle='popover']")
                .popover({
                    content: thisCellRef.@org.jbpm.workbench.pr.client.editors.instance.list.ProcessInstanceSummaryErrorPopoverCell::getPopoverContent().bind(thisCellRef)
                })
                .off("mouseenter click")
                .on("mouseenter", function () {
                    $wnd.jQuery(this).popover("toggle");
                })
                .on("click", function () {
                    $wnd.jQuery(this).popover("hide");
                })
                .off("inserted.bs.popover")
                .on("inserted.bs.popover", function () {
                    var jPopover = $wnd.jQuery(this);
                    $wnd.jQuery("[data-field='" + linkName + "']")
                        .off("click")
                        .on("click", function () {
                            var processInstId = $wnd.jQuery(this).attr(procIdAttrName);
                            thisCellRef.@org.jbpm.workbench.pr.client.editors.instance.list.ProcessInstanceSummaryErrorPopoverCell::openErrorView(Ljava/lang/String;)(processInstId);
                        })
                        .parent()
                        .off("mouseleave click")
                        .on("mouseleave click", function () {
                            jPopover.popover("hide");
                        });
                });
            
        });
    }-*/;
}
