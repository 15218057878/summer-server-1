package cn.cerc.ui.fields;

import cn.cerc.core.DataSet;
import cn.cerc.core.Record;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.IColumn;
import cn.cerc.ui.fields.editor.CheckEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine;
import cn.cerc.ui.other.BuildText;
import cn.cerc.ui.other.SearchItem;
import cn.cerc.ui.parts.UIComponent;

public class BooleanField extends AbstractField implements SearchItem, IColumn, IFieldEvent, IFieldBuildText {
    private String trueText = "是";
    private String falseText = "否";
    private String title;
    private boolean search;
    private CheckEditor editor;
    private String onclick;
    private String oninput;
    private BuildText buildText;

    public BooleanField(UIComponent owner, String title, String field) {
        this(owner, title, field, 0);
    }

    public BooleanField(UIComponent owner, String title, String field, int width) {
        super(owner, title, width);
        this.setField(field);
        this.setAlign("center");
    }

    @Override
    public String getText() {
        Record record = getRecord();
        if (record == null) {
            return null;
        }
        if (buildText != null) {
            HtmlWriter html = new HtmlWriter();
            buildText.outputText(record, html);
            return html.toString();
        }
        return record.getBoolean(field) ? trueText : falseText;
    }

    public BooleanField setBooleanText(String trueText, String falseText) {
        this.trueText = trueText;
        this.falseText = falseText;
        return this;
    }

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
            html.print(getText());
        } else {

            if (!(this.getOwner() instanceof AbstractGridLine)) {
                html.print(getText());
            }

            html.print(getEditor().format(getRecord()));
        }
    }

    @Override
    public void outputLine(HtmlWriter html) {
        if (this.isReadonly()) {
            html.print(this.getName() + "：");
            html.print(this.getText());
        } else {
            if (!this.search) {
                html.println(String.format("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "："));
            }
            html.print(String.format("<input type=\"checkbox\" id=\"%s\" name=\"%s\" value=\"1\"", this.getId(),
                    this.getId()));
            boolean val = false;
            DataSet dataSet = getDataSource() != null ? getDataSource().getDataSet() : null;
            if (dataSet != null) {
                val = dataSet.getBoolean(field);
            }
            if (val) {
                html.print(" checked");
            }
            if (this.isReadonly()) {
                html.print(" disabled");
            }
            if (this.getOnclick() != null) {
                html.print(" onclick=\"%s\"", this.getOnclick());
            }
            html.print(">");

            if (this.search) {
                html.println(String.format("<label for=\"%s\">%s</label>", this.getId(), this.getName()));
            } else {
                if (this.title != null) {
                    html.print("<label for=\"%s\">%s</label>", this.getId(), this.title);
                }
            }
        }
    }

    @Override
    public String getTitle() {
        return title == null ? this.getName() : title;
    }

    public BooleanField setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isSearch() {
        return search;
    }

    @Override
    public void setSearch(boolean search) {
        this.search = search;
    }

    public CheckEditor getEditor() {
        if (editor == null) {
            editor = new CheckEditor(this);
        }
        return editor;
    }

    public String getTrueText() {
        return trueText;
    }

    public void setTrueText(String trueText) {
        this.trueText = trueText;
    }

    public String getFalseText() {
        return falseText;
    }

    public void setFalseText(String falseText) {
        this.falseText = falseText;
    }

    @Override
    public String getOninput() {
        return oninput;
    }

    @Override
    public BooleanField setOninput(String oninput) {
        this.oninput = oninput;
        return this;
    }

    @Override
    public String getOnclick() {
        return onclick;
    }

    @Override
    public BooleanField setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }

    @Override
    public BooleanField createText(BuildText buildText) {
        this.buildText = buildText;
        return this;
    }

    @Override
    public BuildText getBuildText() {
        return buildText;
    }

}
