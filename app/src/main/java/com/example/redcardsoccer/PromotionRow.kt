package com.example.redcardsoccer

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class PromotionRow(): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.promotion_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
