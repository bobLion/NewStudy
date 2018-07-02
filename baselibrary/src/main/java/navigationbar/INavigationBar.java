package navigationbar;

/**
 * @package navigationbar
 * @fileName INavigationBar
 * @Author Bob on 2018/4/20 10:05.
 * @Describe 导航栏的规范
 */

public interface INavigationBar  {
    // 头部的规范
    public int bindLayoutId();

    // 绑定头部的参数
    public void applyView();
}
