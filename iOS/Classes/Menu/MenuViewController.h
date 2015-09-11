#import "BaseVC.h"

#import "SettingsViewController.h"
#import "PPRevealSideViewController.h"
#import "ProfileViewController.h"

@interface MenuViewController : BaseVC<PPRevealSideViewControllerDelegate,UIActionSheetDelegate>
{
    IBOutlet UIButton *btnProfile;
}
-(IBAction)btnAction:(id)sender;

@end
