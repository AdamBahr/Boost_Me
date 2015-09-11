#import <UIKit/UIKit.h>

@class Question;
@interface CellQuestion : UITableViewCell
{
    Question *cellData;
    
    UILabel *lblQuestion;
    
    UILabel *lblAns1;
    UILabel *lblAns2;
    UILabel *lblAns3;
    UILabel *lblAns4;
    
    UILabel *lblOpt1;
    UILabel *lblOpt2;
    UILabel *lblOpt3;
    UILabel *lblOpt4;
    
}
-(void)setCellData:(Question *)data;
-(float)getCellHeight;

@end
