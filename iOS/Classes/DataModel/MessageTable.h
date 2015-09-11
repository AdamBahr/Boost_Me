#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface MessageTable : NSManagedObject

@property (nonatomic, retain) NSNumber * date;
@property (nonatomic, retain) NSString * fId;
@property (nonatomic, retain) NSString * message;
@property (nonatomic, retain) NSString * messageDate;
@property (nonatomic, retain) NSNumber * mid;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * receiverId;
@property (nonatomic, retain) NSNumber * senderId;
@property (nonatomic, retain) NSString * uniqueId;
@property (nonatomic, retain) NSNumber * pt;

@end
