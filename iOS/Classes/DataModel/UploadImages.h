#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface UploadImages : NSManagedObject
@property (nonatomic, retain) NSString * imageUrlLocal;
@property (nonatomic, retain) NSString * imageUrlFB;
@property (nonatomic, retain) NSString * fbId;

@end
