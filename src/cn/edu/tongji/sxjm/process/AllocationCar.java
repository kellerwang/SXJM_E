package cn.edu.tongji.sxjm.process;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import cn.edu.tongji.sxjm.excel.ExcelHelper;
import cn.edu.tongji.sxjm.model.CarResult;
import cn.edu.tongji.sxjm.model.NewResultModel;
import cn.edu.tongji.sxjm.model.RequirementModel;
import cn.edu.tongji.sxjm.model.ResultModel;
import cn.edu.tongji.sxjm.model.SiteModel;
import cn.edu.tongji.sxjm.model.UseRatio;

public class AllocationCar {

	public static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// 单层利用率表
	private static List<UseRatio> listUnderUseRatio_1_1 = new ArrayList<UseRatio>();
	private static List<UseRatio> listUnderUseRatio_1_2 = new ArrayList<UseRatio>();
	// 利用率表
	private static List<UseRatio> listUseRatio = new ArrayList<UseRatio>();

	// 地点列表
	private static Map<String, SiteModel> mapSiteModel;
	// 路线终点记录表
	private static List<String> listTerminal = new ArrayList<String>();

	// I、II、III型车长度
	private final static double length_I = 4.61;
	private final static double length_II = 3.615;
	private final static double length_III = 4.63;

	// I、II、III型车底层最多装载数量
	private final static int maxUnder_I = 5;
	private final static int maxUnder_II = 6;
	private final static int maxUnder_III = 5;

	// I、II、III型车最多装载数量
	private final static int max_I = 15;
	private final static int max_II = 18;
	private final static int max_III = 5;

	// 1_1、1_2型轿运车对应类型号
	public final static int typeCarCarry_1_1 = 1;
	public final static int typeCarCarry_1_2 = 2;

	// 1_1、1_2型轿运车长
	private final static double length_1_1 = 19;
	private final static double length_1_2 = 24.3;

	// 车间间距
	private final static double distanceCar = 0.1;

	// 选择程序标识
	private static int choiceCase = 0;

	// 输入文件名称
	private static String fileName = null;

	// 前三问程序
	public static CarResult question1(String file) {
		RequirementModel rm = ExcelHelper.readExcel1(file);
		init1();
		return allocationCarsRm(rm);
	}

	// 第四问程序
	public static CarResult question4(String file) {
		init1();
		// 初始化终点
		listTerminal.add("A");
		listTerminal.add("C");
		mapSiteModel = ExcelHelper.readExcel4(file);
		CarResult cr = allocationCarWithDifferentSit();
		mapSiteModel = ExcelHelper.readExcel4(file);
		int mileage = optimalAllocation(cr);
		cr.setMileage(mileage);
		return cr;

	}

	// 主程序界面
	public static void CreateJFrame(String title) {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame(title);
		Container container = frame.getContentPane();
		final GridBagLayout gb = new GridBagLayout();
		container.setLayout(gb);
		container.setBackground(Color.CYAN);
		final JPanel panel = new JPanel();
		final JLabel label = new JLabel("输入文件：");
		final JTextField jtf = new JTextField(20);
		jtf.setEditable(false);
		final JButton buttonFilePath = new JButton("选择文件");

		buttonFilePath.addActionListener(new ActionListener() {// 为按钮添加鼠标单击事件
					public void actionPerformed(ActionEvent e) {
						JFileChooser fd = new JFileChooser();
						// fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fd.showOpenDialog(null);
						File f = fd.getSelectedFile();
						if (f != null) {
							fileName = f.toString();
							jtf.setText(fileName);
						}
					}
				});

		final JLabel labelSM = new JLabel("针对前4问：");
		final JLabel label_1_1 = new JLabel("1_1型车数：");
		final JTextField jtf_1_1 = new JTextField(5);
		jtf_1_1.setEditable(false);
		final JLabel label_1_2 = new JLabel("1_2型车数：");
		final JTextField jtf_1_2 = new JTextField(5);
		jtf_1_2.setEditable(false);
		final JLabel labelLC = new JLabel("里程数：");
		final JTextField jtfLC = new JTextField(5);
		jtfLC.setEditable(false);
		JButton button = new JButton("确定");
		button.addActionListener(new ActionListener() {// 为按钮添加鼠标单击事件
			public void actionPerformed(ActionEvent e) {
				System.out.println("确定");
				CarResult cr = null;
				if (fileName != null) {
					switch (choiceCase) {
					case 0:
						cr = question1(fileName);
						jtf_1_1.setText(String.valueOf(cr.getNumCarCarry_1_1()));
						jtf_1_2.setText(String.valueOf(cr.getNumCarCarry_1_2()));
						break;
					case 1:
						cr = question4(fileName);
						jtf_1_1.setText(String.valueOf(cr.getNumCarCarry_1_1()));
						jtf_1_2.setText(String.valueOf(cr.getNumCarCarry_1_2()));
						jtfLC.setText(String.valueOf(cr.getMileage()));
						break;
					case 2:
						JOptionPane.showMessageDialog(null, "程序完善中，方案请参见论文！");
						jtf_1_1.setText("");
						jtf_1_2.setText("");
						jtfLC.setText("");
						break;
					default:
						break;
					}
				} else {
					JOptionPane.showMessageDialog(null, "输入文件不能为空！");
				}
			}
		});

		Choice choice = new Choice();
		choice.add("第1-3问");
		choice.add("第4问");
		choice.add("第5问");
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					if (evt.getItem().toString().equals("第1-3问")) {
						choiceCase = 0;
						jtf.setText("");
					}
					if (evt.getItem().toString().equals("第4问")) {
						choiceCase = 1;
						jtf.setText("");
					}
					if (evt.getItem().toString().equals("第5问")) {
						choiceCase = 2;
						jtf.setText("");
					}
				}
			}
		});

		panel.add(choice);
		panel.add(buttonFilePath);
		panel.add(label);
		panel.add(jtf);
		panel.add(button);
		panel.add(labelSM);
		panel.add(label_1_1);
		panel.add(jtf_1_1);
		panel.add(label_1_2);
		panel.add(jtf_1_2);
		panel.add(labelLC);
		panel.add(jtfLC);
		container.add(panel);

		frame.setSize(1200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	// 初始化单层分配利用率表
	public static void initUnderUseRatio(int type, double length) {
		for (int x_I = 0; x_I <= maxUnder_I; x_I++) {
			for (int x_II = 0; x_II <= maxUnder_II; x_II++) {
				for (int x_III = 0; x_III <= maxUnder_III; x_III++) {
					double temp = ((length_I + distanceCar) * x_I
							+ (length_II + distanceCar) * x_II + (length_III + distanceCar)
							* x_III)
							/ length;
					if (temp > 1) {
						continue;
					} else {
						switch (type) {
						case typeCarCarry_1_1:
							listUnderUseRatio_1_1.add(new UseRatio(x_I, x_II,
									x_III, temp, type, new RequirementModel(
											x_I, x_II, x_III)));
							break;
						case typeCarCarry_1_2:
							listUnderUseRatio_1_2.add(new UseRatio(x_I, x_II,
									x_III, temp, type, new RequirementModel(
											x_I, x_II, x_III)));
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	// 初始化分配利用率表
	public static void initUseRatio(int type, double length) {
		for (int x_I = 0; x_I <= max_I; x_I++) {
			for (int x_II = 0; x_II <= max_II; x_II++) {
				for (int x_III = 0; x_III <= max_III; x_III++) {
					double temp = (length_I + distanceCar) * x_I
							+ (length_II + distanceCar) * x_II
							+ (length_III + distanceCar) * x_III;
					UseRatio urWhole = new UseRatio(x_I, x_II, x_III, type);
					switch (type) {
					case typeCarCarry_1_1:
						if (temp <= length) {
							for (UseRatio ur : listUnderUseRatio_1_1) {
								if (ur.equals(urWhole)) {
									urWhole.setUserRatio(ur.getUserRatio() / 2);
									urWhole.setLayer1(ur.getRm());
									listUseRatio.add(urWhole);
									break;
								}
							}
						} else {
							for (UseRatio ur : listUnderUseRatio_1_1) {
								if (ur.isBestHoldPlan2(urWhole)) {
									int xUpper_I = urWhole.getX_I()
											- ur.getX_I();
									int xUpper_II = urWhole.getX_II()
											- ur.getX_II();

									double tempUpper = (length_I + distanceCar)
											* xUpper_I
											+ (length_II + distanceCar)
											* xUpper_II;
									if (tempUpper <= length) {
										double tempNewUseRatio = (ur
												.getUserRatio() * length + tempUpper)
												/ (length * 2);
										urWhole.setUserRatio(tempNewUseRatio);
										urWhole.setLayer1(ur.getRm());
										urWhole.setLayer2(new RequirementModel(
												xUpper_I, xUpper_II, 0));
										listUseRatio.add(urWhole);
									}
									break;
								}
							}
						}
						break;
					case typeCarCarry_1_2:
						if (temp <= length) {
							for (UseRatio ur : listUnderUseRatio_1_2) {
								if (ur.equals(urWhole)) {
									urWhole.setUserRatio(ur.getUserRatio() / 2);
									urWhole.setLayer1(ur.getRm());
									listUseRatio.add(urWhole);
									break;
								}
							}
						} else {
							for (UseRatio ur : listUnderUseRatio_1_2) {
								if (ur.isBestHoldPlan2(urWhole)) {
									int xUpper_I = urWhole.getX_I()
											- ur.getX_I();
									int xUpper_II = urWhole.getX_II()
											- ur.getX_II();
									double tempUpper = (length_I + distanceCar)
											* xUpper_I
											+ (length_II + distanceCar)
											* xUpper_II;
									UseRatio urUpper = new UseRatio(xUpper_I,
											xUpper_II, 0, type);
									if (tempUpper <= length) {
										for (UseRatio ur2 : listUnderUseRatio_1_2) {
											if (ur2.equals(urUpper)) {
												urWhole.setUserRatio((ur
														.getUserRatio() + ur2
														.getUserRatio()) / 3);
												urWhole.setLayer1(ur.getRm());
												urWhole.setLayer2(ur2.getRm());
												listUseRatio.add(urWhole);
												break;
											}
										}
									} else {
										for (UseRatio ur2 : listUnderUseRatio_1_2) {
											if (ur2.isBestHoldPlan(urUpper)) {
												int xUpper_I_2 = urUpper
														.getX_I()
														- ur2.getX_I();
												int xUpper_II_2 = urUpper
														.getX_II()
														- ur2.getX_II();
												double tempUpper2 = (length_I + distanceCar)
														* xUpper_I_2
														+ (length_II + distanceCar)
														* xUpper_II_2;
												if (tempUpper2 <= length) {
													double tempNewUseRatio = ((ur
															.getUserRatio() + ur2
															.getUserRatio())
															* length + tempUpper2)
															/ (length * 3);
													urWhole.setUserRatio(tempNewUseRatio);
													urWhole.setLayer1(ur
															.getRm());
													urWhole.setLayer2(ur2
															.getRm());
													urWhole.setLayer3(new RequirementModel(
															xUpper_I_2,
															xUpper_II_2, 0));
													listUseRatio.add(urWhole);
												}
												break;
											}
										}
									}
								}
							}
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	// 判断方案结果类别中是否存在该方案，存在则加一
	public static boolean isListResultHavePlan(List<UseRatio> listResult,
			UseRatio plan) {
		boolean result = false;
		for (UseRatio temp : listResult) {
			if (temp.getRm().equals(plan.getRm())
					&& temp.getNumCarCarrier() == plan.getNumCarCarrier()) {
				temp.setNum(temp.getNum() + 1);
				result = true;
				break;
			}
		}
		return result;
	}

	// 根据I、II、III型车的需求总数分配1_1和1_2车数量并输出
	public static CarResult allocationCars(UseRatio ur) {
		List<UseRatio> listResult = new ArrayList<UseRatio>();
		int numCarCarry_1_1 = 0;
		int numCarCarry_1_2 = 0;
		while (!ur.isFinished()) {
			boolean flag = false;
			for (UseRatio temp : listUseRatio) {
				if (temp.isWholeHoldPlan(ur)) {
					switch (temp.getNumCarCarrier()) {
					case typeCarCarry_1_1:
						numCarCarry_1_1++;
						flag = true;
						break;
					case typeCarCarry_1_2:
						numCarCarry_1_2++;
						double limit = numCarCarry_1_1 / 5;
						if (numCarCarry_1_2 > limit) {
							numCarCarry_1_2--;
							continue;
						} else {
							flag = true;
						}
						break;
					default:
						break;
					}
					if (!isListResultHavePlan(listResult, temp)) {
						listResult.add(temp);
					}
					ur.setX_I(ur.getX_I() - temp.getX_I());
					ur.setX_II(ur.getX_II() - temp.getX_II());
					ur.setX_III(ur.getX_III() - temp.getX_III());
					System.out.println(temp.getX_I() + "|" + temp.getX_II()
							+ "|" + temp.getX_III());
					break;
				}
			}
			if (!flag) {
				for (UseRatio temp : listUseRatio) {
					if (temp.isBestHoldPlan3(ur)) {
						switch (temp.getNumCarCarrier()) {
						case typeCarCarry_1_1:
							numCarCarry_1_1++;
							break;
						case typeCarCarry_1_2:
							numCarCarry_1_2++;
							double limit = numCarCarry_1_1 / 5;
							if (numCarCarry_1_2 > limit) {
								numCarCarry_1_2--;
								continue;
							}
							break;
						default:
							break;
						}
						if (!isListResultHavePlan(listResult, temp)) {
							listResult.add(temp);
						}
						ur.setX_I(ur.getX_I() - temp.getX_I());
						ur.setX_II(ur.getX_II() - temp.getX_II());
						ur.setX_III(ur.getX_III() - temp.getX_III());
						System.out.println(temp.getX_I() + "|" + temp.getX_II()
								+ "|" + temp.getX_III());
						break;
					}
				}
			}
		}
		System.out.println("1_1型车共：" + numCarCarry_1_1 + " 1_2型车共："
				+ numCarCarry_1_2);
		ExcelHelper.writeExcel1(listResult, "解-第1-3问.xls");
		return new CarResult(numCarCarry_1_1, numCarCarry_1_2);
	}

	// 根据I、II、III型车的需求总数分配1_1和1_2车数量并输出(输入数值)
	public static void allocationCarsNum(int x_I, int x_II, int x_III) {
		UseRatio ur = new UseRatio(x_I, x_II, x_III);
		allocationCars(ur);
	}

	// 根据I、II、III型车的需求总数分配1_1和1_2车数量并输出(输入数值)
	public static CarResult allocationCarsRm(RequirementModel rm) {
		UseRatio ur = new UseRatio(rm);
		return allocationCars(ur);
	}

	// 写入文件，追加
	public static void writeToFileAppend(String filename, String temp)
			throws IOException {
		FileWriter fw = null;
		fw = new FileWriter(filename, true);
		fw.write(temp);
		fw.close();
	}

	// 初始化利用率
	public static void init1() {
		initUnderUseRatio(typeCarCarry_1_1, length_1_1);
		initUnderUseRatio(typeCarCarry_1_2, length_1_2);
		Collections.sort(listUnderUseRatio_1_1);
		Collections.sort(listUnderUseRatio_1_2);
		initUseRatio(typeCarCarry_1_1, length_1_1);
		initUseRatio(typeCarCarry_1_2, length_1_2);
		Collections.sort(listUseRatio);
	}

	// 初始化地点列表和终点
	public static void initSiteModelList() {
		mapSiteModel = new HashMap<String, SiteModel>();
		SiteModel smD = initSiteModel(41, 0, 0, null, 160);
		SiteModel smC = initSiteModel(33, 47, 0, smD, 76);
		SiteModel smB = initSiteModel(50, 0, 0, smD, 120);
		SiteModel smA = initSiteModel(42, 31, 0, smB, 80);
		mapSiteModel.put("A", smA);
		mapSiteModel.put("B", smB);
		mapSiteModel.put("C", smC);
		mapSiteModel.put("D", smD);
		// 初始化终点
		listTerminal.add("A");
		listTerminal.add("C");
	}

	// 初始化一个地点
	public static SiteModel initSiteModel(int x_I, int x_II, int x_III,
			SiteModel sm, int distanceToFront) {
		return new SiteModel(new RequirementModel(x_I, x_II, x_III), sm,
				distanceToFront);
	}

	// 判断当增加type类型车辆时，是否满足1_2车数量小于等于1_1车数量
	public static boolean isSatisfy20Percent(int numCarCarry_1_1,
			int numCarCarry_1_2, int type) {
		boolean result = true;
		switch (type) {
		case typeCarCarry_1_1:
			break;
		case typeCarCarry_1_2:
			double newNumCarCarry_1_2 = numCarCarry_1_2 + 1;
			double limit = numCarCarry_1_1 / 5;
			if (newNumCarCarry_1_2 > limit) {
				result = false;
			}
			break;
		}
		return result;
	}

	// 判断各地点需求分配是否全部完成
	public static boolean isRequirementFinish() {
		boolean result = true;
		Iterator iter = mapSiteModel.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, SiteModel> entry = (Entry<String, SiteModel>) iter
					.next();
			if (!entry.getValue().isRequirementEmpty()) {
				result = false;
				break;
			}
		}
		return result;
	}

	// 获取一个地点到起点距离和利用率的乘积，返回负值表示改地点所在路线对应分配方案情况不存在
	public static NewResultModel disAndUseRatio2(SiteModel sm, UseRatio plan,
			RequirementModel rm) {
		RequirementModel rmAdd = rm.add(sm.getRm());
		NewResultModel result = new NewResultModel();
		if (sm.getFront() == null) {
			if (rmAdd.compareTo(plan.getRm()) == -1) {
				return null;
			} else {
				result.setMileage(sm.getDistanceToFront());
				result.setDaur(plan.getUserRatio() / sm.getDistanceToFront());
				if (sm.getRm().compareTo(plan.getRm()) == -1) {
					RequirementModel newRm = new RequirementModel(sm.getRm());
					result.setPlan(new UseRatio(newRm.subBoth(plan.getRm()),
							plan.getNumCarCarrier()));
				} else {
					result.setPlan(new UseRatio(0, 0, 0));
				}
				return result;
			}
		} else {
			NewResultModel front = disAndUseRatio2(sm.getFront(), plan, rmAdd);
			if (front == null) {
				return null;
			} else {

				double temp = 0;
				int x_I = (rmAdd.getX_I() > front.getPlan().getRm().getX_I()) ? front
						.getPlan().getRm().getX_I()
						: rmAdd.getX_I();
				int x_II = (rmAdd.getX_II() > front.getPlan().getRm().getX_II()) ? front
						.getPlan().getRm().getX_II()
						: rmAdd.getX_II();
				int x_III = (rmAdd.getX_III() > front.getPlan().getRm()
						.getX_III()) ? front.getPlan().getRm().getX_III()
						: rmAdd.getX_III();
				switch (plan.getNumCarCarrier()) {
				case typeCarCarry_1_1:
					temp = (x_I * length_I + x_II * length_II + x_III
							* length_III)
							/ (length_1_1 * 2);
					break;
				case typeCarCarry_1_2:
					temp = (x_I * length_I + x_II * length_II + x_III
							* length_III)
							/ (length_1_2 * 3);
					break;
				default:
					return null;
				}
				if (temp != 0) {
					result.setMileage(front.getMileage()
							+ sm.getDistanceToFront());
				} else {
					result.setMileage(front.getMileage());
				}
				result.setDaur(temp / sm.getDistanceToFront() + front.getDaur());
				RequirementModel newRm = new RequirementModel(sm.getRm());
				result.setPlan(new UseRatio(newRm.subBoth(front.getPlan()
						.getRm()), front.getPlan().getNumCarCarrier()));
				return result;
			}
		}
	}

	// 获取一个地点到起点距离和利用率的乘积，返回负值表示改地点所在路线对应分配方案情况不存在
	public static ResultModel disAndUseRatio(SiteModel sm, UseRatio plan,
			RequirementModel rm) {
		RequirementModel rmAdd = rm.add(sm.getRm());
		ResultModel result = new ResultModel();
		if (sm.getFront() == null) {
			if (rmAdd.compareTo(plan.getRm()) == -1) {
				return null;
			} else {
				result.setDaur(plan.getUserRatio() * sm.getDistanceToFront());
				result.setMileage(sm.getDistanceToFront());
				return result;
			}
		} else {
			ResultModel front = disAndUseRatio(sm.getFront(), plan, rmAdd);
			if (front == null) {
				return null;
			} else {
				if (rmAdd.compareTo(plan.getRm()) == -1) {
					double temp = 0;
					int x_I = (rmAdd.getX_I() > plan.getRm().getX_I()) ? plan
							.getRm().getX_I() : rmAdd.getX_I();
					int x_II = (rmAdd.getX_II() > plan.getRm().getX_II()) ? plan
							.getRm().getX_II() : rmAdd.getX_II();
					int x_III = (rmAdd.getX_III() > plan.getRm().getX_III()) ? plan
							.getRm().getX_III() : rmAdd.getX_III();
					switch (plan.getNumCarCarrier()) {
					case typeCarCarry_1_1:
						temp = (x_I * length_I + x_II * length_II + x_III
								* length_III)
								/ (length_1_1 * 2);
						break;
					case typeCarCarry_1_2:
						temp = (x_I * length_I + x_II * length_II + x_III
								* length_III)
								/ (length_1_2 * 3);
						break;
					default:
						return null;
					}
					if (temp != 0) {
						result.setMileage(front.getMileage()
								+ sm.getDistanceToFront());
					} else {
						result.setMileage(front.getMileage());
					}
					result.setDaur(temp * sm.getDistanceToFront()
							+ front.getDaur());
					return result;
				} else {
					result.setMileage(front.getMileage()
							+ sm.getDistanceToFront());
					result.setDaur(front.getDaur() + plan.getUserRatio()
							* sm.getDistanceToFront());
					return result;
				}
			}
		}
	}

	// 获取一个地点到起点距离和利用率的乘积，返回负值表示改地点所在路线对应分配方案情况不存在
	public static ResultModel disAndUseRatio3(SiteModel sm, UseRatio plan,
			RequirementModel rm) {
		RequirementModel rmAdd = rm.add(sm.getRm());
		ResultModel result = new ResultModel();
		if (sm.getFront() == null) {
			if (rmAdd.compareTo(plan.getRm()) == -1) {
				return null;
			} else {
				result.setDaur(plan.getUserRatio() * sm.getDistanceToFront()
						* plan.getRm().getSum());
				result.setMileage(sm.getDistanceToFront());
				return result;
			}
		} else {
			ResultModel front = disAndUseRatio3(sm.getFront(), plan, rmAdd);
			if (front == null) {
				return null;
			} else {
				if (rmAdd.compareTo(plan.getRm()) == -1) {
					double temp = 0;
					int x_I = (rmAdd.getX_I() > plan.getRm().getX_I()) ? plan
							.getRm().getX_I() : rmAdd.getX_I();
					int x_II = (rmAdd.getX_II() > plan.getRm().getX_II()) ? plan
							.getRm().getX_II() : rmAdd.getX_II();
					int x_III = (rmAdd.getX_III() > plan.getRm().getX_III()) ? plan
							.getRm().getX_III() : rmAdd.getX_III();
					int sum = x_I + x_II + x_III;
					switch (plan.getNumCarCarrier()) {
					case typeCarCarry_1_1:
						temp = (x_I * length_I + x_II * length_II + x_III
								* length_III)
								/ (length_1_1 * 2) * sum;
						break;
					case typeCarCarry_1_2:
						temp = (x_I * length_I + x_II * length_II + x_III
								* length_III)
								/ (length_1_2 * 3) * sum;
						break;
					default:
						return null;
					}
					if (temp != 0) {
						result.setMileage(front.getMileage()
								+ sm.getDistanceToFront());
					} else {
						result.setMileage(front.getMileage());
					}
					result.setDaur(temp * sm.getDistanceToFront()
							+ front.getDaur());
					return result;
				} else {
					result.setMileage(front.getMileage()
							+ sm.getDistanceToFront());
					result.setDaur(front.getDaur() + plan.getUserRatio()
							* sm.getDistanceToFront() * plan.getRm().getSum());
					return result;
				}
			}
		}
	}

	public static int mileageOfFinishPlan(SiteModel sm, UseRatio plan) {
		SiteModel temp = sm;
		while (temp.getFront() != null) {

			if (!temp.isRequirementEmpty()) {
				break;
			}
			temp = temp.getFront();
		}
		int mileage = 0;
		while (temp != null) {
			mileage += temp.getDistanceToFront();
			temp = temp.getFront();
		}
		return mileage;
	}

	// 地点所在路线对应分配方案刚好可以完成分配
	public static boolean canPlanFinishRequirement(SiteModel sm, UseRatio plan) {
		SiteModel temp = sm;
		RequirementModel rm = new RequirementModel(0, 0, 0);
		while (temp != null) {
			rm = rm.add(temp.getRm());
			temp = temp.getFront();
		}
		return rm.equals(plan.getRm());
	}

	// 地点所在路线对应分配方案刚好可以完成分配
	public static boolean isPathRequirementFinish(SiteModel sm) {
		SiteModel temp = sm;
		RequirementModel rm = new RequirementModel(0, 0, 0);
		while (temp != null) {
			rm = rm.add(temp.getRm());
			temp = temp.getFront();
		}
		return rm.isRequirementEmpty();
	}

	// 更新需求情况
	public static void updateRequirement(UseRatio plan, String terminal) {
		SiteModel temp = mapSiteModel.get(terminal);
		RequirementModel rm = plan.getRm();
		while (temp != null) {
			rm = temp.getRm().subBoth(rm);
			if (rm.isRequirementEmpty()) {
				break;
			}
			temp = temp.getFront();
		}
	}

	// 更新需求情况
	public static RequirementModel updateRequirement2(SiteModel sm,
			RequirementModel plan) {
		if (sm.getFront() == null) {
			return sm.getRm().subBoth(plan);
		} else {
			return sm.getRm().subBoth(updateRequirement2(sm.getFront(), plan));
		}
	}

	// 根据不同地点的不同需求分配轿运车
	public static CarResult allocationCarWithDifferentSit() {
		int numCarCarry_1_1 = 0;
		int numCarCarry_1_2 = 0;
		int allMileage = 0;
		while (!isRequirementFinish()) {
			double max = 0;
			UseRatio planSelected = null;
			int mileageMax = 0;
			boolean label = true;
			String terminalSelected = null;
			for (UseRatio plan : listUseRatio) {
				if (!label) {
					break;
				}
				if (plan.getRm().isRequirementEmpty()) {
					continue;
				}
				if (isSatisfy20Percent(numCarCarry_1_1, numCarCarry_1_2,
						plan.getNumCarCarrier())) {
					for (String terminal : listTerminal) {
						if (isPathRequirementFinish(mapSiteModel.get(terminal))) {
							continue;
						}
						if (canPlanFinishRequirement(
								mapSiteModel.get(terminal), plan)) {
							planSelected = plan;
							mileageMax = mileageOfFinishPlan(
									mapSiteModel.get(terminal), plan);
							terminalSelected = terminal;
							label = false;
							max = 0;
							break;
						}
						if (plan.getRm().isRequirementEmpty()) {
							System.out.println("");
						}
						ResultModel temp = disAndUseRatio3(
								mapSiteModel.get(terminal), plan,
								new RequirementModel(0, 0, 0));
						if (temp == null) {
							continue;
						} else {
							int mileage = temp.getMileage();
							// temp.setDaur(temp.getDaur() / mileage);
							if (temp.getDaur() > max) {
								max = temp.getDaur();
								planSelected = plan;
								terminalSelected = terminal;
								mileageMax = mileage;
							} else if (temp.getDaur() == max) {
								if (mileage > mileageMax) {
									max = temp.getDaur();
									planSelected = plan;
									terminalSelected = terminal;
									mileageMax = mileage;
								}
							}
						}
					}
				}
			}
			if (planSelected != null && terminalSelected != null) {
				allMileage += mileageMax;
				switch (planSelected.getNumCarCarrier()) {
				case typeCarCarry_1_1:
					numCarCarry_1_1++;
					break;
				case typeCarCarry_1_2:
					numCarCarry_1_2++;
					break;
				}
				System.out.println(planSelected.getRm().getX_I() + ","
						+ planSelected.getRm().getX_II() + ","
						+ planSelected.getRm().getX_III() + ","
						+ realTerminal(terminalSelected, planSelected) + ","
						+ planSelected.getNumCarCarrier());
				updateRequirement(planSelected, terminalSelected);
			}
		}
		System.out.println("需要1_1型车：" + numCarCarry_1_1 + "|需要1_2型车："
				+ numCarCarry_1_2 + "|总里程：" + allMileage);
		return new CarResult(numCarCarry_1_1, numCarCarry_1_2);
	}

	// 根据当前需求判断方案的真实
	public static String realTerminal2(String terminal, UseRatio plan) {
		SiteModel temp = mapSiteModel.get(terminal);
		int count = 0;
		Map<Integer, SiteModel> reverse = new HashMap<Integer, SiteModel>();
		while (temp != null) {
			count++;
			reverse.put(count, temp);
			temp = temp.getFront();
		}
		String result = terminal;
		RequirementModel rm = new RequirementModel(0, 0, 0);
		for (int i = count; i >= 0; i--) {
			rm = rm.add(reverse.get(i).getRm());
			if (rm.compareTo(plan.getRm()) != -1) {
				break;
			}
			result += "*";
		}
		return result;
	}

	// 根据当前需求判断方案的真实
	public static String realTerminal(String terminal, UseRatio plan) {
		SiteModel temp = mapSiteModel.get(terminal);
		while (temp.getFront() != null) {
			RequirementModel rm = temp.getRm().subBoth2(plan.getRm());
			if (!temp.getRm().equals(rm)) {
				break;
			}
			terminal = terminal + "+";
			temp = temp.getFront();
		}
		if (terminal.equals("A+")) {
			terminal = "B";
		}
		if (terminal.equals("A++")) {
			terminal = "D";
		}
		if (terminal.equals("C+")) {
			terminal = "D";
		}
		return terminal;
	}

	// 根据当前需求判断方案中停靠点
	public static List<String> passList(String terminal, UseRatio plan) {
		List<String> result = new ArrayList<String>();
		SiteModel temp = mapSiteModel.get(terminal);
		RequirementModel rm = null;
		while (temp.getFront() != null) {
			RequirementModel rmTemp = new RequirementModel(temp.getRm());
			rm = rmTemp.subBoth(plan.getRm());
			if (rm.isRequirementEmpty()) {
				break;
			}
			if (terminal.endsWith("B")) {
				result.add("D");
			}
			if (terminal.endsWith("A")) {
				result.add("B");
				terminal = "B";
			}
			if (terminal.endsWith("C")) {
				result.add("D");
			}
			temp = temp.getFront();
		}
		return result;
	}

	// 已知车分配最优方案，分配路程最优方案
	public static int optimalAllocation(CarResult cr) {
		List<UseRatio> listResult = new ArrayList<UseRatio>();
		int numCarCarry_1_1 = 0;
		int numCarCarry_1_2 = 0;
		int allMileage = 0;
		while (!isRequirementFinish()) {
			double max = 0;
			UseRatio planSelected = null;
			int mileageMax = 0;
			boolean label = true;
			String terminalSelected = null;
			for (UseRatio plan : listUseRatio) {
				if (!label) {
					break;
				}
				if (plan.getRm().isRequirementEmpty()) {
					continue;
				}
				if ((new CarResult(numCarCarry_1_1, numCarCarry_1_2)
						.isSatisfyOptimalCarNum(cr, plan.getNumCarCarrier()))) {
					for (String terminal : listTerminal) {
						if (isPathRequirementFinish(mapSiteModel.get(terminal))) {
							continue;
						}
						if (canPlanFinishRequirement(
								mapSiteModel.get(terminal), plan)) {
							planSelected = plan;
							mileageMax = mileageOfFinishPlan(
									mapSiteModel.get(terminal), plan);
							terminalSelected = terminal;
							label = false;
							max = 0;
							break;
						}
						if (plan.getRm().isRequirementEmpty()) {
							System.out.println("");
						}
						ResultModel temp = disAndUseRatio3(
								mapSiteModel.get(terminal), plan,
								new RequirementModel(0, 0, 0));
						if (temp == null) {
							continue;
						} else {
							int mileage = temp.getMileage();
							// temp.setDaur(temp.getDaur() / mileage);
							if (temp.getDaur() > max) {
								max = temp.getDaur();
								planSelected = plan;
								terminalSelected = terminal;
								mileageMax = mileage;
							} else if (temp.getDaur() == max) {
								if (mileage > mileageMax) {
									max = temp.getDaur();
									planSelected = plan;
									terminalSelected = terminal;
									mileageMax = mileage;
								}
							}
						}
					}
				}
			}
			if (planSelected != null && terminalSelected != null) {
				allMileage += mileageMax;
				switch (planSelected.getNumCarCarrier()) {
				case typeCarCarry_1_1:
					numCarCarry_1_1++;
					break;
				case typeCarCarry_1_2:
					numCarCarry_1_2++;
					break;
				}
				String end = realTerminal(terminalSelected, planSelected);
				planSelected.setEnd(end);
				planSelected.setPass(passList(end, planSelected));
				if (!isListResultHavePlan(listResult, planSelected)) {
					listResult.add(planSelected);
				}
				System.out.println(planSelected.getRm().getX_I() + ","
						+ planSelected.getRm().getX_II() + ","
						+ planSelected.getRm().getX_III() + ","
						+ realTerminal(terminalSelected, planSelected) + ","
						+ planSelected.getNumCarCarrier());
				updateRequirement(planSelected, terminalSelected);
			}
		}
		System.out.println("需要1_1型车：" + numCarCarry_1_1 + "|需要1_2型车："
				+ numCarCarry_1_2 + "|总里程：" + allMileage);
		ExcelHelper.writeExcel1(listResult, "解-第4问.xls");
		return allMileage;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CreateJFrame("乘用车物流运输计划问题");
	}
}
