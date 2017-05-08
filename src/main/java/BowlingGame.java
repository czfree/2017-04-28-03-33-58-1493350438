public class BowlingGame {

	//采用从后向前遍历
	private int thenGrade,thenButOneGrade;  //记录该项后两次的积分，格共享，用于计算每格的最终得分
	
	//格
	class FrameBowling{
		//每一个格的三种状态
		public final static int STRIKE = 1;
		public final static int SPARE = 2;
		public final static int FAULT = 3;
		
		int grade;  //本格得分
		int state;  //本格状态
		int grade1,grade2;  //本格两次发球的得分
		int numBall;   //本格发球数（可能为一发或者两发）
		Character gradeCode1,gradeCode2;
		
		public FrameBowling(Character gradeCode1,Character gradeCode2){  //每格有两个或者一个计分标记
			this.gradeCode1 = gradeCode1;
			this.gradeCode2 = gradeCode2;
			
			numBall = 1;	
			//更新每球得分
			if(gradeCode1 == 'X'){
				grade1 = 10;
			}else{
				grade1 = Character.digit(gradeCode1, 10);
			}
			if(gradeCode2 != null){
				numBall = 2;
				if(gradeCode2 == '/'){
					grade2 = 10 - grade1;
				}else{
					grade2 = Character.digit(gradeCode2, 10);
				}
			}

			//更新该格状态
			if(grade1 == 10) state = STRIKE;
			else if(grade1 + grade2 == 10) state = SPARE;
			else state = FAULT;
		}
		
		public int getGradeWithLast(){ //根据最近两次得分更新该格最终得分
			grade = grade1 + grade2;
			if(state == STRIKE){
				grade += thenGrade + thenButOneGrade;
			}
			else if(state == SPARE){
				grade += thenGrade;
			}	
			return grade;
		}
		
		public void updateLast(){
			//使用该格的状态更新，全局最近两次的击球
			if(numBall == 1){
				thenButOneGrade = thenGrade;
				thenGrade = grade1;
			}
			else{//numBall == 2
				thenGrade = grade1;
				thenButOneGrade = grade2;
			}
		}
		
	}

    public int getBowlingScore(String bowlingCode) {
    	int sumGrade = 0;   //总得分
    	thenGrade = thenButOneGrade = 0 ;   //初始化后项分数值
        bowlingCode = bowlingCode.replace('-', '0'); //将miss换为0，方便处理
        
        String[] sCode = bowlingCode.split("\\|\\|");   
        if(sCode.length == 2) updateLastWithAdd(sCode[1]);  //可能没有附加球
        String[] tCode = sCode[0].split("\\|");
        
        //计算每格得分和总得分
        for(int i = tCode.length -1 ; i >= 0 ; i--){
        	FrameBowling tFrame;
        	if(tCode[i].length() == 2){
        		tFrame = new FrameBowling(tCode[i].charAt(0),tCode[i].charAt(1));
        	}
        	else{
        		tFrame = new FrameBowling(tCode[i].charAt(0), null);
        	}
        	sumGrade += tFrame.getGradeWithLast();
        	tFrame.updateLast();
        }
        
        return sumGrade;
    }
	
	//使用附加球更新后项分数
    public void updateLastWithAdd(String addCode){
    	if(addCode.length() >= 1){
        	if(addCode.charAt(0) == 'X') thenGrade = 10;
        	else{
        		thenGrade = Character.digit(addCode.charAt(0), 10);
        	}
        }
        if(addCode.length() >= 2){
        	if(addCode.charAt(1) == 'X') thenButOneGrade = 10;
        	else if(addCode.charAt(1) == '/'){  //不确定规则中，附加球第二球会不会出现'/'的情况
        		thenButOneGrade = 10 - thenGrade;
        	}
        	else {
				thenButOneGrade = Character.digit(addCode.charAt(1), 10);
			}
        }
    }
}
