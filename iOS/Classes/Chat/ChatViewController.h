#import <UIKit/UIKit.h>
#import "WebServiceHandler.h"
#import "DataBase.h"
#import"MatchedUserList.h"
#import "PPRevealSideViewController.h"
#import "JSDemoViewController.h"

@interface ChatViewController : UIViewController
{
    NSMutableArray *arrAllMatch;
}
@property (strong , nonatomic) IBOutlet UITableView *tblView;

@end

