#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface EditProfile : NSManagedObject

@property (nonatomic, retain) NSString * fbURL;
@property (nonatomic, retain) NSString * localURL;
@property (nonatomic, retain) NSString * userFBID;
@property (nonatomic, retain) NSNumber * position;

@end
