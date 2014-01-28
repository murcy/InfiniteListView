public abstract class InfiniteListView extends ListView {

    private OnLoadDataListener onLoadDataListener;
    private boolean isLoading = false;
    private ProgressBar progressBar;

    public InfiniteListView(Context context) {
        super(context);
        init();
    }

    public InfiniteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfiniteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnLoadDataListener(OnLoadDataListener onLoadDataListener) {
        this.onLoadDataListener = onLoadDataListener;
    }

    public void loadComplete() {
        isLoading = false;
        progressBar.setVisibility(View.GONE);
    }

    private void init() {

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallTitle);
        LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(progressBarParams);
        progressBar.setPadding(10, 10, 10, 10);
        progressBar.setVisibility(View.GONE);

        LinearLayout footerLinearLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerLinearLayout.setGravity(Gravity.CENTER);
        footerLinearLayout.setLayoutParams(layoutParams);
        footerLinearLayout.addView(progressBar);

        addFooterView(footerLinearLayout);

        setOnScrollListener(new InfiniteScrollListener());
    }

    private class InfiniteScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            boolean loadMore = (totalItemCount != 0) && needLoad(firstVisibleItem, visibleItemCount);

            if (!isLoading && loadMore) {
                if (onLoadDataListener != null) {
                    if (onLoadDataListener.onLoadData()) {
                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    protected abstract boolean needLoad(int firstVisible, int visibleCount);

}
