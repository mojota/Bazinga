package com.mojota.bazinga.model;

/**
 * Created by jamie on 17-7-4.
 */

public class MenuInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    int menuId;
    String menuName;
    int menuResId;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuResId() {
        return menuResId;
    }

    public void setMenuResId(int menuResId) {
        this.menuResId = menuResId;
    }
}
