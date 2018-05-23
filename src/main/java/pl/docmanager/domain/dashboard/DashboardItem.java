package pl.docmanager.domain.dashboard;

import pl.docmanager.domain.attachment.Attachment;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.image.Image;
import pl.docmanager.domain.page.Page;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dashboarditem")
public class DashboardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dashboardid", nullable = false)
    private Dashboard dashboard;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "icon", length = 2048)
    private String icon;

    @Enumerated
    @Column(name = "targettype", nullable = false)
    private DashboardTargetType targetType;

    @ManyToOne
    @JoinColumn(name = "targetpageid")
    private Page targetPage;

    @ManyToOne
    @JoinColumn(name = "targetdashboardid")
    private Dashboard targetDashboard;

    @ManyToOne
    @JoinColumn(name = "targetcategoryid")
    private Category targetCategory;

    @ManyToOne
    @JoinColumn(name = "targetattachmentid")
    private Attachment targetAttachment;

    @ManyToOne
    @JoinColumn(name = "targetimageid")
    private Image targetImage;

    @Column(name = "targetexternalurl", length = 2048)
    private String targetExternalUrl;

    @Column(name = "posrow", nullable = false)
    private Integer posRow;

    @Column(name = "poscol", nullable = false)
    private Integer posCol;

    @Column(name = "rowspan")
    private Integer rowSpan;

    @Column(name = "colspan")
    private Integer colSpan;

    @Enumerated
    @Column(name = "state", nullable = false)
    private DashboardItemState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public DashboardTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(DashboardTargetType targetType) {
        this.targetType = targetType;
    }

    public Page getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(Page targetPage) {
        this.targetPage = targetPage;
    }

    public Dashboard getTargetDashboard() {
        return targetDashboard;
    }

    public void setTargetDashboard(Dashboard targetDashboard) {
        this.targetDashboard = targetDashboard;
    }

    public Category getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(Category targetCategory) {
        this.targetCategory = targetCategory;
    }

    public Attachment getTargetAttachment() {
        return targetAttachment;
    }

    public void setTargetAttachment(Attachment targetAttachment) {
        this.targetAttachment = targetAttachment;
    }

    public Image getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(Image targetImage) {
        this.targetImage = targetImage;
    }

    public String getTargetExternalUrl() {
        return targetExternalUrl;
    }

    public void setTargetExternalUrl(String targetExternalUrl) {
        this.targetExternalUrl = targetExternalUrl;
    }

    public Integer getPosRow() {
        return posRow;
    }

    public void setPosRow(Integer posRow) {
        this.posRow = posRow;
    }

    public Integer getPosCol() {
        return posCol;
    }

    public void setPosCol(Integer posCol) {
        this.posCol = posCol;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public DashboardItemState getState() {
        return state;
    }

    public void setState(DashboardItemState state) {
        this.state = state;
    }
}
