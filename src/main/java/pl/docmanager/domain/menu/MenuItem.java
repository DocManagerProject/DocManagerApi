package pl.docmanager.domain.menu;

import pl.docmanager.domain.attachment.Attachment;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.dashboard.Dashboard;
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
@Table(name = "menuitem")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "menuid", nullable = false)
    private Menu menu;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Enumerated
    @Column(name = "targettype", nullable = false)
    private MenuItemTargetType targetType;

    @ManyToOne
    @JoinColumn(name = "targetmenuid")
    private Menu targetMenu;

    @Column(name = "targeturl", length = 2048)
    private String targetUrl;

    @ManyToOne
    @JoinColumn(name = "targetpageid")
    private Page targetPage;

    @ManyToOne
    @JoinColumn(name = "targetcategoryid")
    private Category targetCategory;

    @ManyToOne
    @JoinColumn(name = "targetdashboardid")
    private Dashboard targetDashboard;

    @ManyToOne
    @JoinColumn(name = "targetattachmentid")
    private Attachment targetAttachment;

    @ManyToOne
    @JoinColumn(name = "targetimageid")
    private Image targetImage;

    @Column(name = "index", nullable = false)
    private int index;

    @Enumerated
    @Column(name = "state", nullable = false)
    private MenuItemState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItemTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(MenuItemTargetType targetType) {
        this.targetType = targetType;
    }

    public Menu getTargetMenu() {
        return targetMenu;
    }

    public void setTargetMenu(Menu targetMenu) {
        this.targetMenu = targetMenu;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Page getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(Page targetPage) {
        this.targetPage = targetPage;
    }

    public Category getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(Category targetCategory) {
        this.targetCategory = targetCategory;
    }

    public Dashboard getTargetDashboard() {
        return targetDashboard;
    }

    public void setTargetDashboard(Dashboard targetDashboard) {
        this.targetDashboard = targetDashboard;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MenuItemState getState() {
        return state;
    }

    public void setState(MenuItemState state) {
        this.state = state;
    }
}
