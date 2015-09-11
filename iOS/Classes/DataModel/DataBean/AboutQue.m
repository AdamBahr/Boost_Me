#import "AboutQue.h"

@implementation AboutQue

@synthesize abt_id;
@synthesize detail;
@synthesize your_ans;

-(id)initWithDict:(NSDictionary *)dictData
{
    self=[super init];
    if (self) {
        if (dictData) {
            abt_id=[dictData objectForKey:@"abt_id"];
            detail=[dictData objectForKey:@"detail"];
            your_ans=[dictData objectForKey:@"your_ans"];
        }
    }
    return self;
}

@end
