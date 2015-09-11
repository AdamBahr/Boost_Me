#import "DetailQue.h"

@implementation DetailQue

@synthesize d_id,detail,your_ans,type;

-(id)initWithDict:(NSDictionary *)dictData
{
    self=[super init];
    if (self) {
        if (dictData) {
            d_id=[dictData objectForKey:@"d_id"];
            detail=[dictData objectForKey:@"detail"];
            your_ans=[dictData objectForKey:@"your_ans"];
            type= [[dictData objectForKey:@"type"] integerValue];
        }
    }
    return self;
}

@end
