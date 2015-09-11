#import <Foundation/Foundation.h>

@interface Speak : NSObject

@property(nonatomic,copy)NSString *lang;
@property(nonatomic,copy)NSString *frean;

-(id)initWithDict:(NSDictionary *)dictData;

@end
