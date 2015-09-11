#import <UIKit/UIKit.h>

@interface ProfileViewController : UIViewController<UIScrollViewDelegate,UIGestureRecognizerDelegate,PPRevealSideViewControllerDelegate>
{
   
}
@property (nonatomic, strong) IBOutlet UIPageControl *pageCtrl;
@property (nonatomic, strong) IBOutlet UIImageView *mainImageView;
@property (nonatomic, strong) IBOutlet UIScrollView *mainScrollView;
@property (nonatomic, strong) IBOutlet UIView *bottomContainer;
@property (nonatomic, strong) IBOutlet UILabel *lblNameAndAge;


@end
