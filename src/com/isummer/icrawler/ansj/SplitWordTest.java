package com.isummer.icrawler.ansj;

import org.ansj.splitWord.analysis.ToAnalysis;

import com.isummer.icrawler.util.SplitWordUtils;

public class SplitWordTest {

	public static void main(String[] args) {
		
		ToAnalysis.parse("");
		String words = "�й��������Ĵ������Ź�֮һ�������ƾõ���ʷ�����Լ5000��ǰ������ԭ����Ϊ���Ŀ�ʼ���־�����֯�����ɹ��Һͳ���������������ݱ�ͳ�������������ʱ��ϳ��ĳ������ġ��̡��ܡ����������ơ��Ρ�Ԫ��������ȡ���ԭ������ʷ�ϲ����뱱���������彻������ս���ڶ������ںϳ�Ϊ�л����塣20���ͳ������������й��ľ��������˳���ʷ��̨��ȡ����֮���ǹ������塣1949���л����񹲺͹����������й���½�����������������ƶȵ����塣�й����Ŷ�ʵ������Ļ�����ͳ������ʽ��ʫ�ʡ�Ϸ�����鷨�͹����ȣ����ڡ�Ԫ�������������硢������������й���Ҫ�Ĵ�ͳ���ա�";  
        System.out.println(SplitWordUtils.parse(words));
        words = "�й��������Ĵ������Ź�֮һ�������ƾõ���ʷ�����Լ5000��ǰ������ԭ����Ϊ���Ŀ�ʼ���־�����֯�����ɹ��Һͳ���������������ݱ�ͳ�������������ʱ��ϳ��ĳ������ġ��̡��ܡ����������ơ��Ρ�Ԫ��������ȡ���ԭ������ʷ�ϲ����뱱���������彻������ս���ڶ������ںϳ�Ϊ�л����塣20���ͳ������������й��ľ��������˳���ʷ��̨��ȡ����֮���ǹ������塣1949���л����񹲺͹����������й���½�����������������ƶȵ����塣�й����Ŷ�ʵ������Ļ�����ͳ������ʽ��ʫ�ʡ�Ϸ�����鷨�͹����ȣ����ڡ�Ԫ�������������硢������������й���Ҫ�Ĵ�ͳ���ա�";  
        System.out.println(SplitWordUtils.parse(words));
	}

}