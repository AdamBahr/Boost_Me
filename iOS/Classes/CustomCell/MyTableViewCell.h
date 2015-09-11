#import <UIKit/UIKit.h>
@class RoundedImageView;
@interface MyTableViewCell : UITableViewCell
//@property(nonatomic,strong)  UIImageView *myImage;
@property(nonatomic,strong) RoundedImageView *roundImageView;
@property(nonatomic,strong) UILabel *lblName;
-(void)setImage:(UIImage *)image;

@end
