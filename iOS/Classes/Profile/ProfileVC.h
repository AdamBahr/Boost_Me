#import <UIKit/UIKit.h>

@interface ProfileVC : UIViewController<UIScrollViewDelegate,PPRevealSideViewControllerDelegate>{
    NSMutableArray *arrImages;
    int currentPage;
}
@property(nonatomic,strong)User *user;

@property(nonatomic,weak)IBOutlet UIScrollView *scrImage;
@property(nonatomic,weak)IBOutlet UIPageControl *pcImage;
@property(nonatomic,weak)IBOutlet UILabel *lblNameAndAge;
@property(nonatomic,weak)IBOutlet UITextView *txtAbout;
@property(nonatomic,weak)IBOutlet UILabel *lblAway;
@property(nonatomic,weak)IBOutlet UILabel *lblActive;

@end