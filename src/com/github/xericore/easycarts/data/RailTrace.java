package com.github.xericore.easycarts.data;

import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.util.Vector;

public class RailTrace
{
    private static final double Degrees90FromRad = Math.PI/2;

    private BlockFace _nextFacing;
    private Block _nextBlock;

    public RailTrace(Block initialBlock, BlockFace initialFacing)
    {
        if(initialFacing == null)
            return;

        BlockFace adjustedInitialFacing = getNextFacingFromRailShape(getTracedRailFromBlock(initialBlock).getShape(), initialFacing);

        Vector initialDirection = Utils.getDirectionFromBlockFace(adjustedInitialFacing);
        Location nextLocation = initialBlock.getLocation().clone().add(initialDirection);

        _nextBlock = nextLocation.getBlock();

        TracedRail nextTracedRail = getNextTracedRail();

        _nextFacing = getNextFacingFromRailShape(nextTracedRail.getShape(), adjustedInitialFacing);
    }

    public BlockFace getNextFacing()
    {
        return _nextFacing;
    }

    public Block getNextBlock()
    {
        return _nextBlock;
    }

    public TracedRail getNextTracedRail()
    {
        return getTracedRailFromBlock(_nextBlock);
    }

    private TracedRail getTracedRailFromBlock(Block block)
    {
        if(block.getBlockData().getMaterial() != Material.RAIL)
            return null;

        Rail nextRail = (Rail) block.getBlockData();
        return new TracedRail(nextRail.getShape(), block.getLocation());
    }

    public static BlockFace getNextFacingFromRailShape(Rail.Shape currentRailShape, BlockFace initialFacing)
    {
        if(currentRailShape == null)
            return null;

        double rotateAround = 0d;

        switch (currentRailShape)
        {
            case NORTH_SOUTH:
            case EAST_WEST:
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
                return initialFacing;
            case SOUTH_EAST:
                switch (initialFacing)
                {
                    case NORTH:
                    case EAST:
                    case NORTH_EAST:
                    case NORTH_WEST:
                        return BlockFace.EAST;
                    case SOUTH:
                    case WEST:
                    case SOUTH_WEST:
                    case SOUTH_EAST:
                        return BlockFace.SOUTH;
                }
                break;
            case SOUTH_WEST:
                switch (initialFacing)
                {
                    case NORTH:
                    case WEST:
                    case SOUTH_WEST:
                    case NORTH_WEST:
                        return BlockFace.WEST;
                    case SOUTH:
                    case EAST:
                    case NORTH_EAST:
                    case SOUTH_EAST:
                        return BlockFace.SOUTH;
                }
                break;
            case NORTH_WEST:
                switch (initialFacing)
                {
                    case NORTH:
                    case EAST:
                    case NORTH_EAST:
                    case NORTH_WEST:
                        return BlockFace.NORTH;
                    case SOUTH:
                    case WEST:
                    case SOUTH_WEST:
                    case SOUTH_EAST:
                        return BlockFace.WEST;
                }
                break;
            case NORTH_EAST:
                switch (initialFacing)
                {
                    case SOUTH:
                    case EAST:
                    case SOUTH_EAST:
                    case SOUTH_WEST:
                        return BlockFace.EAST;
                    case NORTH:
                    case WEST:
                    case NORTH_WEST:
                    case NORTH_EAST:
                        return BlockFace.NORTH;
                }
                break;
        }

        Vector previousDirection = Utils.getDirectionFromBlockFace(initialFacing);
        Vector newDirection = previousDirection.clone().rotateAroundY(rotateAround);
        return Utils.getBlockFaceFromDirection(newDirection);
    }
}
