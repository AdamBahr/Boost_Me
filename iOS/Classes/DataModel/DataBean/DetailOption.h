#import <Foundation/Foundation.h>

@interface DetailOption : NSObject

@property(nonatomic,copy)NSString *idD;
@property(nonatomic,copy)NSString *flag;
@property(nonatomic,copy)NSString *options;

-(id)initWithDict:(NSDictionary *)dictData;

@end
