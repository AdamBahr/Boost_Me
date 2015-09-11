#import <Foundation/Foundation.h>

@interface UserSettings : NSObject
{
    
}
@property(nonatomic,copy)NSString *sex;
@property(nonatomic,copy)NSString *prRad;
@property(nonatomic,copy)NSString *prSex;
@property(nonatomic,copy)NSString *prLAge;
@property(nonatomic,copy)NSString *prUAge;

+(UserSettings *)currentSetting;

@end


