#import "DetailOption.h"

@implementation DetailOption

@synthesize idD;
@synthesize flag;
@synthesize options;

-(id)initWithDict:(NSDictionary *)dictData
{
    self=[super init];
    if (self) {
        if (dictData) {
            idD=[dictData objectForKey:@"id"];
            flag=[dictData objectForKey:@"flag"];
            options=[dictData objectForKey:@"options"];
        }
    }
    return self;
}

@end
