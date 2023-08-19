package androidx.recyclerview.widget



abstract class ModuleSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {


    public override fun getCachedSpanIndex(position: Int, spanCount: Int): Int {
        return super.getCachedSpanIndex(position, spanCount)
    }

    public override fun getCachedSpanGroupIndex(position: Int, spanCount: Int): Int {
        return super.getCachedSpanGroupIndex(position, spanCount)
    }

}
