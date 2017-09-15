package com.project.golovolomki.MainActivityViewList;

public class ViewListItem {
    public String name, description, complexity, state, favorite;
    public int index;

    public ViewListItem() {}

    public ViewListItem(int indexValue, String nameValue, String descriptionValue, String complexityValue,
                        String stateValue, String favoriteValue) {
        index           = indexValue;
        name            = nameValue;
        description     = descriptionValue;
        complexity      = complexityValue;
        state           = stateValue;
        favorite        = favoriteValue;
    }
}