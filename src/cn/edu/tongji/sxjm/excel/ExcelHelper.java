package cn.edu.tongji.sxjm.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.tongji.sxjm.model.RequirementModel;
import cn.edu.tongji.sxjm.model.SiteModel;
import cn.edu.tongji.sxjm.model.UseRatio;
import cn.edu.tongji.sxjm.process.AllocationCar;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelHelper {
	// 写execl
	public static void writeExcel1(List<UseRatio> listResult, String fileName) {
		try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File(fileName));
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test
			int title = 0;

			// 将定义好的单元格添加到工作表中
			sheet.addCell(new Label(0, title, "轿用车类型"));
			sheet.addCell(new Label(1, title, "相同类型、相同装载方式的车辆数"));
			sheet.addCell(new Label(2, title, "装在上层I型乘用车数量"));
			sheet.addCell(new Label(3, title, "装在上层II型乘用车数量"));
			sheet.addCell(new Label(4, title, "装在上层III型乘用车数量"));
			sheet.addCell(new Label(5, title, "装在下层I型乘用车数量"));
			sheet.addCell(new Label(6, title, "装在下上层II型乘用车数量"));
			sheet.addCell(new Label(7, title, "装在下层III型乘用车数量"));
			sheet.addCell(new Label(8, title, "中间停靠地"));
			sheet.addCell(new Label(9, title, "目的地"));

			int count = 1;
			for (UseRatio plan : listResult) {
				switch (plan.getNumCarCarrier()) {
				case AllocationCar.typeCarCarry_1_1:
					sheet.addCell(new Label(0, count, "1_1"));
					sheet.addCell(new jxl.write.Number(2, count, plan
							.getLayer2().getX_I()));
					sheet.addCell(new jxl.write.Number(3, count, plan
							.getLayer2().getX_II()));
					sheet.addCell(new jxl.write.Number(4, count, plan
							.getLayer2().getX_III()));
					sheet.addCell(new jxl.write.Number(5, count, plan
							.getLayer1().getX_I()));
					sheet.addCell(new jxl.write.Number(6, count, plan
							.getLayer1().getX_II()));
					sheet.addCell(new jxl.write.Number(7, count, plan
							.getLayer1().getX_III()));
					break;
				case AllocationCar.typeCarCarry_1_2:
					sheet.addCell(new Label(0, count, "1_2"));
					RequirementModel rm = plan.getLayer2()
							.add(plan.getLayer3());
					sheet.addCell(new jxl.write.Number(2, count, rm.getX_I()));
					sheet.addCell(new jxl.write.Number(3, count, rm.getX_II()));
					sheet.addCell(new jxl.write.Number(4, count, rm.getX_III()));
					sheet.addCell(new jxl.write.Number(5, count, plan
							.getLayer1().getX_I()));
					sheet.addCell(new jxl.write.Number(6, count, plan
							.getLayer1().getX_II()));
					sheet.addCell(new jxl.write.Number(7, count, plan
							.getLayer1().getX_III()));
					break;
				default:
					break;
				}
				sheet.addCell(new jxl.write.Number(1, count, plan.getNum()));
				String strTemp = "";
				for (String str : plan.getPass()) {
					strTemp += str;
				}
				sheet.addCell(new Label(8, count, strTemp));
				sheet.addCell(new Label(9, count, plan.getEnd()));
				count++;
			}
			/*
			 * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
			 */

			// 写入数据并关闭文件
			book.write();
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 读execl
	public static RequirementModel readExcel1(String fileName) {
		try {
			int x_I = 0;
			int x_II = 0;
			int x_III = 0;
			Workbook book = Workbook.getWorkbook(new File(fileName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cellType = sheet.getCell(0, i);
				Cell cellNum = sheet.getCell(1, i);
				String type = cellType.getContents();
				int num = Integer.parseInt(cellNum.getContents());
				if (type.equals("I")) {
					x_I += num;
				}
				if (type.equals("II")) {
					x_II += num;
				}
				if (type.equals("III")) {
					x_III += num;
				}
			}
			book.close();
			return new RequirementModel(x_I, x_II, x_III);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	// 读execl
	public static Map<String, SiteModel> readExcel4(String fileName) {
		try {
			Workbook book = Workbook.getWorkbook(new File(fileName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			RequirementModel rmA = new RequirementModel();
			RequirementModel rmB = new RequirementModel();
			RequirementModel rmC = new RequirementModel();
			RequirementModel rmD = new RequirementModel();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cellType = sheet.getCell(0, i);
				Cell cellA = sheet.getCell(1, i);
				Cell cellB = sheet.getCell(2, i);
				Cell cellC = sheet.getCell(3, i);
				Cell cellD = sheet.getCell(4, i);
				String type = cellType.getContents();
				int numA = Integer.parseInt(cellA.getContents());
				int numB = Integer.parseInt(cellB.getContents());
				int numC = Integer.parseInt(cellC.getContents());
				int numD = Integer.parseInt(cellD.getContents());
				if (type.equals("I")) {
					rmA.setX_I(numA);
					rmB.setX_I(numB);
					rmC.setX_I(numC);
					rmD.setX_I(numD);
				}
				if (type.equals("II")) {
					rmA.setX_II(numA);
					rmB.setX_II(numB);
					rmC.setX_II(numC);
					rmD.setX_II(numD);
				}
				if (type.equals("III")) {
					rmA.setX_III(numA);
					rmB.setX_III(numB);
					rmC.setX_III(numC);
					rmD.setX_III(numD);
				}
			}
			book.close();
			Map<String, SiteModel> mapSiteModel = new HashMap<String, SiteModel>();
			SiteModel smD = new SiteModel(rmD, null, 160);
			SiteModel smC = new SiteModel(rmC, smD, 76);
			SiteModel smB = new SiteModel(rmB, smD, 120);
			SiteModel smA = new SiteModel(rmA, smB, 80);
			mapSiteModel.put("A", smA);
			mapSiteModel.put("B", smB);
			mapSiteModel.put("C", smC);
			mapSiteModel.put("D", smD);
			return mapSiteModel;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
