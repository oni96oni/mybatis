package com.conn.db;

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
import com.emp.vo.JobCountVO;

public class DBConn {
	public static SqlSession getSqlSession() {
		SqlSession sess = null;
		
		String config = "resources/mybatis-config.xml";
		InputStream is;
		
		try {
			is = Resources.getResourceAsStream(config);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is,"development");
			sess = sqlSessionFactory.openSession(false); // 자동커밋을 사용안하겠다!
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
		
		DataVO insertData = new DataVO();
//		insertData.setId(1);
		insertData.setName("test");
		insertData.setToday(new Date(new java.util.Date().getTime())); // import java.sql.Date;
		
		int result7 = session.insert("empMapper.dataInsert1", insertData);
		session.commit(); // 직접 커밋할때 사용
		System.out.println(result7 + " 개 행이 추가되었습니다.");
		
		
//		DataVO checkData = session.selectOne("empMapper.inserCheck", insertData.getId());
//		if(checkData == null) {
//			int result7 = session.insert("empMapper.dataInsert1", insertData); // <mapper namespace="empMapper"> + <insert id="dataInsert1" parameterType="dataVo"> = namespace.id
//			session.commit(); // 직접 커밋할때 사용
//			System.out.println(result7 + " 개 행이 추가되었습니다.");
//		} else {
//			System.out.println("id 컬럼에 " + insertData.getId() + " 에 해당하는 데이터가 이미 존재합니다.");
//			session.rollback();
//		}
//		이 과정을 SEQ_MYBATIS.NEXTVAL을 사용함으로써 생략이 가능하다!!
		
		
//		Map<String, Object> updateData = new HashMap<String, Object>(); // map사용 업데이트
//		updateData.put("id", 1);
//		updateData.put("name", "update");
		
//		DataVO updateData = new DataVO(); // 객체사용 업데이트
//		updateData.setId(1);
//		updateData.setName("change");
//		
//		int result8 = session.update("empMapper.dataUpdate1", updateData);
//		session.commit();
//		System.out.println(result8 + " 개 행이 업데이트 되었습니다.");
//		
//		int result9 = session.update("empMapper.dataDelete1", 1);
//		session.commit();
//		System.out.println(result9 + "개 행이 삭제 되었습니다.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date stDate = null, edDate = null;
		
		try {
			stDate = sdf.parse("1990/01/01");
			edDate = sdf.parse("1999/12/31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		EmpWhereVO dynamicData = new EmpWhereVO();
		dynamicData.setSalary(10000);
		dynamicData.setDeptId(80);
		dynamicData.setStartDate(new Date(stDate.getTime()));
		dynamicData.setEndDate(new Date(edDate.getTime()));
		
		List<Integer> deptList = new ArrayList<Integer>();
		deptList.add(80);
		deptList.add(90);
		deptList.add(100);
		
		dynamicData.setDeptIdList(deptList);
		
		List<EmpVO> result10 = session.selectList("empMapper.dynamicQuery", dynamicData);
		System.out.println(result10.size() + "개 행의 데이터가 조회 되었습니다.");
		
		JobCountVO jobData = new JobCountVO();
		String[] searchJob = {"AD_PRES", "AD_VP"};
		jobData.setJobId(searchJob);
		
		List<JobCountVO> result11 = session.selectList("empMapper.dynamicQuery2", jobData);
		System.out.println(result11.size() + "개 행의 데이터가 조회 되었습니다.");
	}
}