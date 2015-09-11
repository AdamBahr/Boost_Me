#import <Foundation/Foundation.h>


typedef enum : NSUInteger{
    TypeSingalSelection=1,
    TypeMultiSelection=2,
    TypeHeight=3,
    TypeSection=4,
    TypeSpeaks=5
}TypeDetailQue;

@interface DetailQue : NSObject

@property(nonatomic,copy)NSString *d_id;
@property(nonatomic,copy)NSString *detail;
@property(nonatomic,copy)NSString *your_ans;
@property(nonatomic,assign)TypeDetailQue type;

-(id)initWithDict:(NSDictionary *)dictData;

@end
