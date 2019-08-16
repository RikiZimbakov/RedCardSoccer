package com.example.redcardsoccer

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.promotion_row.view.*

class PromotionRow(val promotionObject:PromotionObject): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.promotion_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.description_textview_promotion_row.text = promotionObject.description
        Picasso.get().load(promotionObject.imageUrl).into(viewHolder.itemView.imageview_promotion_row)
        //change name of profileImageUrl later to ImageUrl
    }
}
