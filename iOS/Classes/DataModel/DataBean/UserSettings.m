#import "UserSettings.h"

@implementation UserSettings

@synthesize sex,prRad,prSex,prLAge,prUAge;

#pragma mark -
#pragma mark - Init

-(id)init{
    
    if((self = [super init]))
    {
        
    }
    return self;
}

+(UserSettings *)currentSetting
{
    static UserSettings *obj = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        obj = [[UserSettings alloc] init];
    });
    return obj;
}

@end
