package com.codesense.driverapp.ui.drawer;

public class NavDrawerItem {

    private String title;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
    private String iconName;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, String icon) {
        this.title = title;
        this.iconName = icon;

    }

    public NavDrawerItem(String title, boolean isCounterVisible,
                         String count) {
        this.title = title;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCount() {
        return this.count;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
