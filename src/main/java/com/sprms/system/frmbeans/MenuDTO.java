package com.sprms.system.frmbeans;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {

    private Long id;
    private String menuName;
    private String menuUrl;
    private String icon;
    private Integer displayOrder;
    private List<MenuDTO> children = new ArrayList<>();
    
    public MenuDTO() {}

    public MenuDTO(Long id, String menuName, String menuUrl) {
        this.id = id;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.children = new ArrayList<>();
    }
    public MenuDTO(Long id, String menuName, String menuUrl, String icon, Integer displayOrder) {
        this.id = id;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.icon = icon;
        this.displayOrder = displayOrder;
        this.children = new ArrayList<>();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public List<MenuDTO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuDTO> children) {
		this.children = children;
	}

   
}