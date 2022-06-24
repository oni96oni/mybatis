package com.conn.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.emp.vo.DataVO;
import com.emp.vo.EmpVO;
import com.emp.vo.EmpWhereVO;

public class DBConn {

	public static SqlSession getSqlSession() {
		SqlSession sess = null;
		
		String config = "resources/mybatis-config.xml";
		InputStream is;
		
		try {
			is = Resources.getResourceAsStream(config);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is, "development");
			sess = sqlSessionFactory.openSession(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sess;
	}
	
	public static void main(String[] args) {
		SqlSession session = DBConn.getSqlSession();
		int result1 = session.selectOne("empMapper.empCount");
		System.out.println(result1);
		
		List<String> result2 = session.selectList("empMapper.empNames");
		System.out.println(result2);
		
		List<Object> result3 = session.selectList("empMapper.empDatas");
		System.out.println(result3);
		
		for(int idx = 0; idx < result3.size(); idx++) {
			Map<String, Object> data = (Map<String, Object>)(result3.get(idx));
			System.out.println(data.get("EMPLOYEE_ID") + ", " + data.get("FIRST_NAME"));
		}
		
		List<EmpVO> result4 = session.selectList("empMapper.empObjects");
		for(EmpVO data: result4) {
			System.out.println(data.getEmpId() + ", " + data.getFirstName());
		}
		
		EmpVO result5 = session.selectOne("empMapper.empSelect", 100);
		System.out.println(result5.getEmpId()  + ", " + result5.getFirstName());
		
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("first", 100);
		param.put("last", 110);
		List<EmpVO> result6 = session.selectList("empMapper.empRange", param);
		for(EmpVO data: result6) {
			System.out.println(data.getEmpId() + ", " + data.getFirstName());
		}
		
		DataVO insertData = new DataVO();
		// insertData.setId(5);
		insertData.setName("test");
		insertData.setToday(new Date(new java.util.Date().getTime()));
		
		int result7 = session.insert("empMapper.dataInsert1", insertData);
		session.commit();
		System.out.println(result7 + " 개 행이 추가되었습니다.");
		
//		DataVO checkData = session.selectOne("empMapper.insertCheck", insertData.getId());
//		if(checkData == null) {
//			int result7 = session.insert("empMapper.dataInsert1", insertData);
//			session.commit();
//			System.out.println(result7 + " 개 행이 추가되었습니다.");
//		} else {
//			System.out.println("id 컬럼에 " + insertData.getId() + " 에 해당하는 데이터가 이미 존재합니다.");
//			session.rollback();
//		}
		
//		// Map<String, Object> updateData = new HashMap<String, Object>();
//		// updateData.put("id", 1);
//		// updateData.put("name", "update");
//		DataVO updateData = new DataVO();
//		updateData.setId(1);
//		updateData.setName("change");
//		int result8 = session.update("empMapper.dataUpdate1", updateData);
//		session.commit();
//		System.out.println(result8 + " 개 행이 업데이트 되었습니다.");
//		
//		int result9 = session.update("empMapper.dataDelete1", 1);
//		session.commit();
//		System.out.println(result9 + " 개 행이 삭제 되었습니다.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date stDate = null, edDate = null;
		try {
			stDate = sdf.parse("1990/01/01");
			edDate = sdf.parse("1999/12/31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		EmpWhereVO dynamicData = new EmpWhereVO();
		// dynamicData.setSalary(10000);
		// dynamicData.setDeptId(80);
		// dynamicData.setStartDate(new Date(stDate.getTime()));
		// dynamicData.setEndDate(new Date(edDate.getTime()));
		
		List<Integer> deptList = new ArrayList<Integer>();
		deptList.add(80); deptList.add(90); deptList.add(100);
		// dynamicData.setDeptIdList(deptList);
		
		List<EmpVO> result10 = session.selectList("empMapper.dynamicQuery", dynamicData);
		System.out.println(result10.size() + " 개 행 데이터가 조회 되었습니다.");
		
		// Map<String, Integer> paramData = new HashMap<String, Integer>();
		// paramData.put("deptId", 10);
		// paramData.put("stDeptId", 10);
		// paramData.put("edDeptId", 40);
		Map<String, List<Integer>> paramData = new HashMap<String, List<Integer>>();
		List<Integer> deptList2 = new ArrayList<Integer>();
		deptList2.add(10); deptList2.add(20); deptList2.add(40); deptList2.add(70);
		paramData.put("deptList", deptList2);
		
		List<Map<String, Object>> result11 = session.selectList("empMapper.empOfDeptCount", paramData);
		
		for(Map<String, Object> d: result11) {
			System.out.println(d);
		}
		
		Map<String, Object> paramData2 = new HashMap<String, Object>();
		paramData2.put("name", "newSeq");
		paramData2.put("date", new Date(new java.util.Date().getTime()));
		int id = insertSeqData(paramData2);
		if(id != -1) {
			System.out.println("ID 가 " + id + " 인 데이터가 추가 되었습니다.");
		}
		
		DataVO dataVo2 = new DataVO();
		dataVo2.setName("mybatis_sequence");
		dataVo2.setToday(new Date(new java.util.Date().getTime()));
		int result12 = session.insert("empMapper.insertGetSeq", dataVo2);
		if(result12 ==1) {
			System.out.println("ID 가 " + dataVo2.getId() + " 인 데이터가 추가 되었습니다.");
			session.commit();
		}
		
	}
	
	public static int insertSeqData(Map<String, Object> param) {
		SqlSession session = DBConn.getSqlSession();
		int seq = session.selectOne("empMapper.getSeq");
		param.put("seq", seq);
		int result = session.insert("empMapper.insertSeq", param);
		
		if(result == 1) {
			session.commit();
			return seq;
		}
		session.rollback();
		return -1;
	}

}
