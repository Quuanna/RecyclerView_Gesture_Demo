package com.anna.recyclerviewgesturedemo.constant

enum class UseCaseType(val title: String) {
    SWIPE_DOWN_REFRESH("下拉更新"),
    BASIC_DRAG_POSITION_UP_DOWN_MOVE("上下拖拉移動"),
    BASIC_SWIPE_LEFT_RIGHT_REMOVE("左滑/右滑刪除"),
    CUSTOM_SWIPE_LEFT_RIGHT_ADD_BACKGROUND("左滑/右滑刪除 + 背景、文字、icon"),
    BASIC_SWIPE_AND_DRAG("拖拉移動和滑動刪除的組合"),
    BASIC_SWIPE_LEFT_RIGHT_SHOW_BUTTON_EVENT("左滑/右滑動顯示按鈕"),

}