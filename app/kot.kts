class MyAdapter(private val items: List<MyItem>) : SequenceAdapter<MyAdapter.MyItem>() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): MyItem {
        return items[position]
    }

    override fun bindView(sequenceStep: SequenceStep, item: MyItem) {
        with(sequenceStep) {
            setActive(item.isActive)
            setAnchor(item.formattedDate)
            setAnchorTextAppearance(...)
            setTitle(item.title)
            setTitleTextAppearance()
            setSubtitle(...)
            setSubtitleTextAppearance(...)
        }
    }

    data class MyItem(val isActive: Boolean,
                      val formattedDate: String,
                      val title: String)
}