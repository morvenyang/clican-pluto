//
//  OfflineRecordTableItem.m
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import "OfflineRecordTableItem.h"

@implementation OfflineRecordTableItem

@synthesize deleteButton = _deleteButton;
@synthesize actionButton = _actionButton;

-(void)dealloc{
    [super dealloc];
    TT_RELEASE_SAFELY(_deleteButton);
    TT_RELEASE_SAFELY(_actionButton);
}

- (id)initWithCoder:(NSCoder*)decoder {
	if (self = [super initWithCoder:decoder]) {
		self.deleteButton = [decoder decodeObjectForKey:@"deleteButton"];
        self.actionButton = [decoder decodeObjectForKey:@"actionButton"];
	}
	return self;
}

- (void)encodeWithCoder:(NSCoder*)encoder {
	[super encodeWithCoder:encoder];
	if (self.deleteButton) {
		[encoder encodeObject:self.deleteButton forKey:@"deleteButton"];
	}
    if (self.actionButton) {
		[encoder encodeObject:self.actionButton forKey:@"actionButton"];
	}
}
@end
