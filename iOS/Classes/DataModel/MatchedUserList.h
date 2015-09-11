#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface MatchedUserList : NSManagedObject

@property (nonatomic, retain) NSString * fId;
@property (nonatomic, retain) NSString * fName;
@property (nonatomic, retain) NSString * lastActive;
@property (nonatomic, retain) NSString * proficePic;
@property (nonatomic, retain) NSString * status;

@end
