package cn.lee.bookstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 流式的布局
 * @author Administrator
 *
 */
public class FlowLayout extends ViewGroup{
	private final int DEFAULT_SPACING = 12;//默认的间距
	private int horizontalSpacing = DEFAULT_SPACING;//水平间距
	private int verticalSpacing = DEFAULT_SPACING;//垂直间距
	
	//用来存放Line对象的
	private ArrayList<Line> lineList = new ArrayList<Line>();
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}
	/**
	 * 设置水平间距
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing){
		if(horizontalSpacing>0){
			this.horizontalSpacing = horizontalSpacing;
		}
	}
	/**
	 * 设置垂直间距
	 * @param verticalSpacing
	 */
	public void setVerticalSpacing(int verticalSpacing){
		if(verticalSpacing>0){
			this.verticalSpacing = verticalSpacing;
		}
	}
	/**
	 * 给所有的子View进行分行，相当于排好座位表了
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//每次测量之前先清空lineList
		lineList.clear();
		
		//1.获取FlowLayout的宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		//2.获取实际用于比较的宽度
		int noPaddingWidth = width-getPaddingLeft()-getPaddingRight();
		
		//3.遍历所有的View对象，在遍历过程中去分行
		Line line = new Line();//创建Line，用于存放view对象
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);//获得当前的子View
			childView.measure(0, 0);//为了保证能够获取到宽高
			
			//4.如果当前line中木有View，那么就直接将child加入进来，目的是要保证每行
			//至少有一个子VIew，不能允许空行的存在
			if(line.getViewList().size()==0){
				line.addLineView(childView);
			}else if(line.getLineWidth()+horizontalSpacing+childView.getMeasuredWidth()>noPaddingWidth) {
				//5.如果大于noPaddingWidth，说明child需要放入下一行,
				//先记录当前的Line，再去创建新的Line
//				lineList.add(line);
//
//				line = new Line();//创建新Line，用来存放childView
//				line.addLineView(childView);
			}else {
				//6.如果不大于noPaddingWidth，说明child需要放到当前Line
				line.addLineView(childView);
			}
			
			//7.如果当前child是最后一个子VIew，那么应该保存最后的line
			if(i==(getChildCount()-1)){
				lineList.add(line);//保存最后的line对象
			}
		}
		
		//for循环结束后，lineList存放了所有的Lin对象，而每个line又记录了当前行的所有VIew对象
		//8.计算当前FlowLayout所需要的高度
		int measuredHeight = getPaddingBottom()+getPaddingTop();
		for (int i = 0; i < lineList.size(); i++) {
			measuredHeight += lineList.get(i).getLineHeight();//再加上所有行的高度
		}
		measuredHeight += (lineList.size()-1)*verticalSpacing;//最后再加上所有的垂直间距
		
		//9.将高度设置给当前FlowLayout
		setMeasuredDimension(width, measuredHeight);
	}
	
	/**
	 * 摆放所有的View对象，相当于让每个人都坐到自己的位置上去
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//获取当前line对象
			
			//从第二行开始，当前line的top等于上一行的top+上一行的高度+垂直间距
			if(i>0){
				paddingTop += lineList.get(i-1).getLineHeight()+verticalSpacing;
			}
			
			ArrayList<View> viewList = line.getViewList();//获取view集合
			

			for (int j = 0; j < viewList.size(); j++) {
				View childView = viewList.get(j);
				//将perSpacing增加到当前childView的宽度上面
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()),MeasureSpec.EXACTLY);
				childView.measure(widthMeasureSpec,0);//重新测量View
				
				if(j==0){
					//摆放当前line中第一个view对象
					childView.layout(paddingLeft,paddingTop,paddingLeft+childView.getMeasuredWidth(),
							paddingTop+childView.getMeasuredHeight());
				}else {
					//摆放当前line中后面的view，则需要参照前一个VIew的right
					View preView = viewList.get(j-1);
					int left = preView.getRight()+horizontalSpacing;
					childView.layout(left,preView.getTop(),left+childView.getMeasuredWidth(),preView.getBottom());
				}
			}
		}
	}
	/**
	 * 获取当前line的留白宽度
	 * @param line
	 * @return
	 */
	private int getLineRemainSpacing(Line line){
		return getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getLineWidth();
	}
	
	/**
	 * 行对象，用来封装每行的数据
	 * @author Administrator
	 *
	 */
	class Line{
		private ArrayList<View> viewList;//用来存放每行的所有View对象
		private int width;//表示当前行所有View对象的宽+水平间距
		private int height;//表示当前行的高度
		public Line(){
			viewList = new ArrayList<View>();
		}
		/**
		 * 记录当前行的VIew对象
		 * @param view
		 */
		public void addLineView(View view){
			if(!viewList.contains(view)){
				viewList.add(view);
				
				//更新宽度
				if(viewList.size()==1){
					//第一个添加进来的view不需要加水平间距
					width = view.getMeasuredWidth();
				}else {
					//如果不是第一个View，那么应该在当前width的基础上+view的宽度+水平间距
					width += view.getMeasuredWidth() + horizontalSpacing;
				}
				
				//更新高度,总是记录当前行中高度最高的那个
				height = Math.max(height,view.getMeasuredHeight());
			}
		}
		
		/**
		 * 获取viewList
		 * @return
		 */
		public ArrayList<View> getViewList(){
			return viewList;
		}
		
		/**
		 * 获取当前行的宽度
		 * @return
		 */
		public int getLineWidth(){
			return width;
		}
		/**
		 * 获取当前行的高度
		 * @return
		 */
		public int getLineHeight(){
			return height;
		}
	}
	
}
