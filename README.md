# Android RecyclerView ItemTouchHelper Use Case

<img src="https://github.com/Quuanna/RecyclerViewGestureDemo/assets/36694083/825a02df-70cf-4e34-870f-8f2ecc0223fb" width="250px">
<img src="https://github.com/Quuanna/RecyclerViewGestureDemo/assets/36694083/c9d77540-406d-4e45-ab75-a771c72f28ff" width="250px">
<img src="https://github.com/Quuanna/RecyclerViewGestureDemo/assets/36694083/8c0b98f7-084e-45c7-ba1e-3922c53948a7" width="250px">


## ItemTouchHelper

- 可監聽 RecyclerView ViewHolder ItemView 的滑動交互行為，可以方向（向右或向左）或移動（向上或向下）
- ItemTouchHelper.Callback 回傳手勢行為的方法
    - dragFlags -  項目可以拖動的方向。
    - swipeFlags - 項目可以滑動的方向。
    
    ```kotlin
    public static int makeMovementFlags(int dragFlags, int swipeFlags) {
           return makeFlag(ACTION_STATE_IDLE, swipeFlags | dragFlags)
                        | makeFlag(ACTION_STATE_SWIPE, swipeFlags)
                        | makeFlag(ACTION_STATE_DRAG, dragFlags);
    } 
    ```
    
- ItemTouchHelper.SimpleCallback (`SimpleCallback extends Callback`)
    - dragDirs – 可以在其中拖曳視圖的方向標誌的二進位或。必須由LEFT 、 RIGHT 、 START 、 END 、 UP和DOWN組成。
    - swipeDirs – 可以滑動視圖的方向標誌的二進位或。必須由LEFT 、 RIGHT 、 START 、 END 、 UP和DOWN組成。
    
    ```kotlin
    public SimpleCallback(int dragDirs, int swipeDirs) {
            mDefaultSwipeDirs = swipeDirs;
            mDefaultDragDirs = dragDirs;
    }  
    ```
    

---

## UseCase 1. 上下拖動換位置

### drag 基本原理

> ItemTouchHelper實作執行拖放，從一個位置移動到新位置時將會呼叫onMove方法。
> 
- Code
    
    ```kotlin
    class ItemTouchHelperCallback(val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {
    
        override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    
        
        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            // 上下移動位置
            return true
        }
    }
    ```
    

---

## UseCase 2. 直接滑掉 Item，左滑右滑

### Swipe 基本原理

> ItemTouchHelper 平移項目時時顯示的背景空間，複寫自訂義行為、繪製(Canvas)，
透過onChildDraw 方法中監聽平移時的空間可透過 dx值判斷，使用Canvas 繪製 item 平移後的空間背景、文字、icon
> 
- Code
    
    ```kotlin
    val itemTouchHelper = ItemTouchHelper(simpleCallback())
    itemTouchHelper.attachToRecyclerView(recyclerView)
    
     private fun simpleCallback() = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
    
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
    
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> // TODO 當使用者向左滑動時執行某些操作
                    ItemTouchHelper.RIGHT -> // TODO 當使用者向右滑動時執行某些操作
                }
            }
          
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                // TODO 當用戶滑動時，開始裝飾的地方，Item 加入背景、文字、圖標
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    
    ```
---

## UseCase 3. 滑動Item顯示Button

同理UseCase 只是在onChildDraw時使用drawButton

> 透過onChildDraw 方法負責繪製滑動按鈕，根據滑動距離計算按鈕的可用空間，並呼叫每個按鈕的 draw 方法，clearView方法重置 swipedPosition 並啟動滑動項目的恢復，按鈕 click 透過onTouchListener 攔截給適當的按鈕處理。
> 

定義了一個名為 SwipeButtonDrawHelper 的自定義 ItemTouchHelper.SimpleCallback，用於在 RecyclerView 中添加滑動顯示按鈕的功能

```kotlin
 val itemTouchHelper = ItemTouchHelper(object :
            SwipeButtonDrawHelper(binding.recyclerView, ItemTouchHelper.LEFT) {
            override fun instantiateButton(position: Int): List<SwipeButton> {
                
                var buttons = listOf<SwipeButton>()
				        val deleteButton = deleteButton(position)
				        val markAsUnreadButton = markAsUnreadButton(position)
				        val archiveButton = archiveButton(position)
				        when (position) {
				            1 -> buttons = listOf(deleteButton)
				            2 -> buttons = listOf(deleteButton, markAsUnreadButton)
				            3 -> buttons = listOf(deleteButton, markAsUnreadButton, archiveButton)
				            else -> Unit
				        }
				        return buttons

            }
  })
  itemTouchHelper.attachToRecyclerView(binding.recyclerView)
  
  
   private fun deleteButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Deleted item $position")
                }
            })
    }
    
    private fun markAsUnreadButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Mark as unread",
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Marked as unread item $position")
                }
            })
    }

    private fun archiveButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Archive",
            14.0f,
            android.R.color.holo_blue_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Archived item $position")
                }
            })
    }
```

## 參考
- [How To add swipe Left to Right in recyclerview ](https://stackoverflow.com/questions/64366919/how-to-add-swipe-left-to-right-in-recyclerview)
- [原理 Part1 ~ Part3 ](https://medium.com/@acerezoluna/part-3-recyclerview-from-zero-to-hero-397b7996280)
