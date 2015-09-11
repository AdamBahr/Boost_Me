#import <Foundation/Foundation.h>

@interface AboutQue : NSObject

@property(nonatomic,copy)NSString *abt_id;
@property(nonatomic,copy)NSString *detail;
@property(nonatomic,copy)NSString *your_ans;

-(id)initWithDict:(NSDictionary *)dictData;

@end
