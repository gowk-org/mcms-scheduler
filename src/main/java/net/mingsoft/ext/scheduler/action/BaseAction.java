package net.mingsoft.ext.scheduler.action;

import java.util.MissingResourceException;

/**
 * scheduler基础控制层
 *
 * @author z
 * 创建日期：2020-4-2 17:12:52
 * 历史修订：
 * @version $Id: $Id
 */
public class BaseAction extends net.mingsoft.basic.action.BaseAction{

	/** {@inheritDoc} */
	@Override
	protected String getResString(String key) {
		// TODO Auto-generated method stub
		String str = "";
		try {
			str = super.getResString(key);
		} catch (MissingResourceException e) {
			str = net.mingsoft.ext.scheduler.constant.Const.RESOURCES.getString(key);
		}

		return str;
	}

}
