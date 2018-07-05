# master 仿今日头条搜索控件

如何使用
Step 1. Add the JitPack repository to your build file
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2. Add the dependency
  
  	dependencies {
	        implementation 'com.github.whffire:master:1.0'
	}
  
  
  
  ---------------------------
  布局中
  <com.tools.fj.searchview.SearchView
        android:id="@+id/search_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"


        />
        
  代码中      
   
        searchView.setTYPE(1);  // 1代码searchview存储的搜索记录类型，为INT，项目中设置不同type 记录不同界面的搜索记录
        
        
        searchView.sethint("请输入检索内容");
        
        
        //设置头部颜色，默认跟coloraccent一样
        searchView.setBackoundColor(getResources().getColor(R.color.color_light_blue));
        
        
        //搜索点击事件
          searchView.setOnSearchClikckListener(new OnSearchClikckListener() {
            @Override
            public void onSearchClick(String keyword) {
            
         //   包括小键盘点击事件和头部搜索按钮点击事件，触发网络请求进行搜索
            Toast.makeText(MainActivity.this, "onSearchClick="+keyword, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVisible(int Visible) {
            //返回搜索结果列表需要隐藏或显示属性
                Toast.makeText(MainActivity.this, "onVisible=" + Visible, Toast.LENGTH_SHORT).show();
                view.setVisibility(Visible);
               
               
               
               如有不明白  QQ84569945
               
               
               
                
                
            }
        });
