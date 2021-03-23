package cn.cerc.ui.fields;

import cn.cerc.core.Record;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.IColumn;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.fields.editor.ColumnEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine;
import cn.cerc.ui.other.BuildText;
import cn.cerc.ui.other.BuildUrl;
import cn.cerc.ui.parts.UIComponent;

public class StringField extends AbstractField implements IColumn, IFieldDialog, IFieldPlaceholder, IFieldPattern,
        IFieldRequired, IFieldAutofocus, IFieldBuildText, IFieldBuildUrl {
    private ColumnEditor editor;
    private DialogField dialog;
    private String placeholder;
    private String pattern;
    private boolean required;
    private boolean autofocus;
    private String icon;
    private BuildUrl buildUrl;
    private BuildText buildText;
    private UIComponent helper;

    public StringField(UIComponent owner, String name, String field) {
        super(owner, name, 0);
        this.setField(field);
    }

    public StringField(UIComponent owner, String name, String field, int width) {
        super(owner, name, 0);
        this.setField(field);
        this.setWidth(width);
    }

    @Override
    public String getText() {
        Record record = getRecord();
        if (record != null) {
            if (this instanceof IFieldBuildText) {
                IFieldBuildText obj = (IFieldBuildText) this;
                if (obj.getBuildText() != null) {
                    HtmlWriter html = new HtmlWriter();
                    obj.getBuildText().outputText(record, html);
                    return html.toString();
                }
            }
            return record.getString(getField());
        } else {
            return null;
        }
    }

    public ColumnEditor getEditor() {
        if (editor == null) {
            editor = new ColumnEditor(this);
        }
        return editor;
    }

    @Override
    public DialogField getDialog() {
        return dialog;
    }

    @Override
    public AbstractField setDialog(String dialogfun) {
        this.dialog = new DialogField(dialogfun);
        dialog.setInputId(this.getId());
        return this;
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public StringField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public StringField setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public StringField setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public boolean isAutofocus() {
        return autofocus;
    }

    @Override
    public StringField setAutofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public StringField setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public StringField createUrl(BuildUrl buildUrl) {
        this.buildUrl = buildUrl;
        return this;
    }

    @Override
    public BuildUrl getBuildUrl() {
        return buildUrl;
    }

    @Override
    public StringField createText(BuildText buildText) {
        this.buildText = buildText;
        return this;
    }

    @Override
    public BuildText getBuildText() {
        return buildText;
    }

    // 隐藏输出
    @Override
    public void outputHidden(HtmlWriter html) {
        html.print("<input");
        html.print(" type=\"hidden\"");
        html.print(" id=\"%s\"", this.getId());
        html.print(" name=\"%s\"", this.getId());
        String value = this.getText();
        if (value != null) {
            html.print(" value=\"%s\"", value);
        }
        html.println("/>");
    }

    @Override
    public void outputColumn(HtmlWriter html) {
        if (this.isReadonly()) {
            if (buildUrl != null) {
                UrlRecord url = new UrlRecord();
                buildUrl.buildUrl(getRecord(), url);
                if (!"".equals(url.getUrl())) {
                    html.print("<a href=\"%s\"", url.getUrl());
                    if (url.getTitle() != null) {
                        html.print(" title=\"%s\"", url.getTitle());
                    }
                    if (url.getTarget() != null) {
                        html.print(" target=\"%s\"", url.getTarget());
                    }
                    html.println(">%s</a>", getText());
                } else {
                    html.println(getText());
                }
            } else {
                html.print(getText());
            }
        } else {
            if ((this.getOwner() instanceof AbstractGridLine)) {
                html.print(getEditor().format(getRecord()));
            } else {
                html.print(getText());
            }
        }
    }

    @Override
    public void outputLine(HtmlWriter html) {
        if (this.isReadonly()) {
            html.print(this.getName() + "：");
            html.print(this.getText());
        } else {
            html.print("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "：");
            html.print("<input");
            if (getHtmType() != null) {
                html.print(" type=\"%s\"", this.getHtmType());
            } else {
                html.print(" type=\"text\"");
            }
            html.print(" id=\"%s\"", this.getId());
            html.print(" name=\"%s\"", this.getId());
            String value = this.getText();
            if (value != null) {
                html.print(" value=\"%s\"", value);
            }
            if (this.getValue() != null) {
                html.print(" value=\"%s\"", this.getValue());
            }
            if (this.isReadonly()) {
                html.print(" readonly=\"readonly\"");
            }
            if (this.getCssClass() != null) {
                html.print(" class=\"%s\"", this.getCssClass());
            }
            if (this.isAutofocus()) {
                html.print(" autofocus");
            }
            if (this.isRequired()) {
                html.print(" required");
            }
            if (this.getPlaceholder() != null) {
                html.print(" placeholder=\"%s\"", this.getPlaceholder());
            }
            if (this.getPattern() != null) {
                html.print(" pattern=\"%s\"", this.getPattern());
            }
            html.println("/>");

            html.print("<span>");
            if (this instanceof IFieldDialog) {
                IFieldDialog obj = (IFieldDialog) this;
                DialogField dialog = obj.getDialog();
                if (dialog != null && dialog.isOpen()) {
                    html.print("<a href=\"%s\">", dialog.getUrl());
                    if (obj.getIcon() != null) {
                        html.print("<img src=\"%s\">", obj.getIcon());
                    } else {
                        html.print("<img src=\"%s\">", CDN.get(config.getClassProperty("icon", "")));
                    }
                    html.print("</a>");
                    return;
                }
            }
            html.println("</span>");
        }
    }

    public UIComponent getHelper() {
        return helper;
    }

    public UIComponent setHelper(UIComponent helper) {
        this.helper = helper;
        return this;
    }

    @Deprecated
    private UIComponent setMark(UIComponent helper) {
        this.helper = helper;
        return this;
    }

}
