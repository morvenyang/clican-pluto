//
//  DBProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import <Foundation/Foundation.h>
#import "OfflineRecord.h"
@interface OfflineRecordProcess : NSObject{
    NSString* _database;
    NSMutableDictionary* _offlineRecords;
}
@property (nonatomic, copy) NSString *database;
@property (nonatomic, retain) NSMutableDictionary* offlineRecords;

-(void) insertOrUpdateOffileRecord:(OfflineRecord*) offlineRecord;
-(void) deleteOffileRecord:(OfflineRecord*) offlineRecord;
-(NSArray*) getAllOfflineRecord;
@end
