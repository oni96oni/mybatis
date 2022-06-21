package com.conn.db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.emp.vo.EmpVO;

public class DBConn {
	public static SqlSession getSqlSession() {
		SqlSession sess = null;
		
		String config = "resources/mybatis-config.xml";
		InputStream is;
		
		try {
			is = Resources.getResourceAsStream(config);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is,"development");
			sess = sqlSessionFactory.openSession(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sess;
	}
	
	public static void main(String[] args) {
		SqlSession session = DBConn.getSqlSession();
		System.out.println(session.getConfiguration().getEnvironment().getDataSource().toString());
		int result = session.selectOne("empMapper.empCount");
		System.out.println(result);
		System.out.println("mybatis01.DBConn파일체크");
		System.out.println("result--------------------------------------------------------------------------");
		
		List<String> result2 = session.selectList("empMapper.empNames");
		System.out.println(result2);
		System.out.println("result2--------------------------------------------------------------------------");
		
		List<Object> result3 = session.selectList("empMapper.empDatas");
		System.out.println(result3);
		System.out.println("result3--------------------------------------------------------------------------");
		
		for(int idx=0; idx< result3.size(); idx++) {
			Map<String,Object> data = (Map<String, Object>)(result3.get(idx));
			System.out.println(data.get("EMPLOYEE_ID")+ ", " + data.get("FIRST_NAME"));
		}
		System.out.println("data.get(\"EMPLOYEE_ID\")+ \", + data.get(\"FIRST_NAME\")--------------------------------------------------------------------------");
		
		Map<Object, Object> result33 = session.selectMap("empMapper.empDatas","EMPLOYEE_ID");
		System.out.println(result33);
		System.out.println("result33--------------------------------------------------------------------------");
		
		List<EmpVO> result4 = session.selectList("empMapper.empObjects");
		for(EmpVO data: result4) {
			System.out.println(data.getEmpId()+ ", "+ data.getFirstName());
		}
		System.out.println("result4--------------------------------------------------------------------------");
		
		EmpVO result5 = session.selectOne("empMapper.empSelect",100);
		System.out.println(result5.getEmpId() + ", " + result5.getFirstName());
		System.out.println("result5--------------------------------------------------------------------------");
		
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("first", 100);
		param.put("last", 110);
		List<EmpVO> result6 = session.selectList("empMapper.empRange", param);
		for(EmpVO data: result6) {
			System.out.println(data.getEmpId() + ", " + data.getFirstName());
		}
		System.out.println("result6--------------------------------------------------------------------------");
	}
}