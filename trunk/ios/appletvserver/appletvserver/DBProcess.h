//
//  DBProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import <Foundation/Foundation.h>
#import "OfflineRecord.h"
@interface DBProcess : NSObject{
    NSString* _database;
}
@property (nonatomic, copy) NSString *database;

-(void) insertOrUpdateOffileRecord:(OfflineRecord*) offlineRecord;
-(void) deleteOffileRecord:(OfflineRecord*) offlineRecord;
-(NSArray*) getAllOfflineRecord;
@end
