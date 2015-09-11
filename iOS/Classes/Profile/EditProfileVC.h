#import <UIKit/UIKit.h>

@interface EditProfileVC : UIViewController<UIActionSheetDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIAlertViewDelegate,UITextFieldDelegate>
{
    int selectedBtnTag;
    NSMutableArray *arrImages;
}
@property(nonatomic,copy)NSString *strStatus;
@property(nonatomic,weak)IBOutlet UITextField *txtStatus;

-(IBAction)onClickChangeStatus:(id)sender;
-(IBAction)onClickBtn:(id)sender;
-(IBAction)onClickImage:(id)sender;

@end
